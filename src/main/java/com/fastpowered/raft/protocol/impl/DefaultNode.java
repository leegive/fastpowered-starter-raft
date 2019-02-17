package com.fastpowered.raft.protocol.impl;

import com.fastpowered.raft.dto.*;
import com.fastpowered.raft.rpc.Request;
import com.fastpowered.raft.rpc.Response;
import com.fastpowered.raft.state.CurrentTerm;
import com.fastpowered.raft.state.VotedFor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.fastpowered.raft.protocol.NodeStatus.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
public class DefaultNode extends AbstractNode {


    @Override
    public RvoteResult handlerPreRequestVote(RvoteParam param) {
        return null;
    }

    @Override
    public RvoteResult handlerRequestVote(RvoteParam param) {
        return null;
    }

    @Override
    public AentryResult handlerAppendEntries(AentryParam param) {
        return null;
    }

    @Override
    public ClientResponse handlerClientRequest(ClientRequest request) {
        return null;
    }

    @Override
    public ClientResponse redirect(ClientRequest request) {
        return null;
    }

    class ElectionTask implements Runnable {
        @Override
        public void run() {
            if (status == LEADER) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            electionTime = electionTime + ThreadLocalRandom.current().nextInt(50);
            if (currentTime - preElectionTime < electionTime) {
                return;
            }

            status = CANDIDATE;
            if (log.isDebugEnabled()) {
                log.debug(
                    "Node {} will become CANDIDATE and start election Leader, current term: {}, last entry: {}",
                    cluster.getSelf(), CurrentTerm.get(),logModule.last()
                );
            }
            preElectionTime = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200) + 150;
            CurrentTerm.increase();
            VotedFor.set(cluster.getSelf());

            ArrayList<Future> remotes = new ArrayList<>(cluster.getPeers().size());
            if (log.isDebugEnabled()) {
                log.debug("Peers size : {}, peer list content : {}", cluster.getPeers().size(), cluster.getPeers());
            }

            cluster.getPeers().forEach(peer -> remotes.add(threadPool.submit(() -> {
                long lastTerm = 0L;
                LogEntry last = logModule.last();
                if (last != null) {
                    lastTerm = last.getTerm();
                }

                RvoteParam param = new RvoteParam();
                param.setTerm(CurrentTerm.get());
                param.setCandidateId(cluster.getSelf().getServerId());
                param.setLastLogIndex(1);
                param.setLastLogTerm(lastTerm);

                Request request = new Request(Request.R_VOTE, param, peer.getServerId());
                Response<RvoteResult> response = raftClient.send(request);
                return response;
            })));

            AtomicInteger successCount = new AtomicInteger(0);
            CountDownLatch latch = new CountDownLatch(remotes.size());

            remotes.forEach(future -> threadPool.submit(() -> {
                try {
                    Response<RvoteResult> response = (Response<RvoteResult>) future.get(3000, MILLISECONDS);
                    if (response == null) {
                        return -1;
                    }
                    boolean voteGranted = response.getResult().isVoteGranted();
                    if (voteGranted) {
                        successCount.incrementAndGet();
                    } else {
                        CurrentTerm.compareAndSet(response.getResult().getTerm());
                    }
                    return 0;
                } finally {
                    latch.countDown();
                }
            }));

            try {
                latch.await(3500, MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int success = successCount.get();
            if (status == FOLLOWER) {
                return;
            }

            if (success >= cluster.getPeers().size() / 2) {
                status = LEADER;
                cluster.setLeader(cluster.getSelf());
                nextIndexs = new ConcurrentHashMap<>();
                matchIndexs = new ConcurrentHashMap<>();
                cluster.getPeers().forEach(peer -> {
                    nextIndexs.put(peer, logModule.lastIndex() + 1);
                    matchIndexs.put(peer, 0L);
                });
            }
            VotedFor.reset();
        }
    }

    class HeartBBeatTask implements Runnable {
        @Override
        public void run() {
            if (status != LEADER) {
                return;
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - preHeartBeatTime < heartBeatTick) {
                return;
            }

            if (log.isDebugEnabled()) {
                cluster.getPeers().forEach(peer -> log.info("Peer {} nexIndex = {}", peer.getServerId(), nextIndexs.get(peer)));
            }

            preHeartBeatTime =System.currentTimeMillis();

            cluster.getPeers().forEach(peer -> {
                AentryParam param = new AentryParam();
                param.setLeaderId(cluster.getSelf().getServerId());
                param.setServerId(peer.getServerId());
                param.setTerm(CurrentTerm.get());

                Request<AentryParam> request = new Request<>(Request.A_ENTRIES, param,peer.getServerId());

                threadPool.execute(()->{
                    Response response = raftClient.send(request);
                    AentryResult result = (AentryResult) response.getResult();
                    long term = result.getTerm();
                    CurrentTerm.compareAndSet(term,()->{
                        VotedFor.reset();
                        status = FOLLOWER;
                    });
                });
            });

        }
    }


}
