package com.fastpowered.raft.protocol.impl;

import com.fastpowered.raft.dto.*;
import com.fastpowered.raft.rpc.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import static com.fastpowered.raft.protocol.NodeStatus.CANDIDATE;
import static com.fastpowered.raft.protocol.NodeStatus.LEADER;

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

    private class ElectionTask implements Runnable {
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
                    cluster.getSelf(),currentTerm,logModule.last()
                );
            }
            preElectionTime = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200) + 150;
            currentTerm++;
            votedFor = cluster.getSelf().getServerId();

            ArrayList<Future> futures = new ArrayList<>(cluster.getPeers().size());
            if (log.isDebugEnabled()) {
                log.debug("Peers size : {}, peer list content : {}", cluster.getPeers().size(), cluster.getPeers());
            }

            cluster.getPeers().forEach(peer -> futures.add(threadPool.submit(() -> {
                long lastTerm = 0L;
                LogEntry last = logModule.last();
                if (last != null) {
                    lastTerm = last.getTerm();
                }

                RvoteParam param = new RvoteParam();
                param.setTerm(currentTerm);
                param.setCandidateId(cluster.getSelf().getServerId());
                param.setLastLogIndex(1);
                param.setLastLogTerm(lastTerm);

                Request request = new Request(Request.R_VOTE, param, peer.getServerId());

                return null;
            })));


        }
    }


}
