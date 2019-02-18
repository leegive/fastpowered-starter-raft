package com.fastpowered.raft.protocol;

import com.fastpowered.raft.current.RaftThreadPool;
import com.fastpowered.raft.dto.LogEntry;
import com.fastpowered.raft.dto.RvoteParam;
import com.fastpowered.raft.dto.RvoteResult;
import com.fastpowered.raft.rpc.RaftClient;
import com.fastpowered.raft.rpc.Request;
import com.fastpowered.raft.rpc.Response;
import com.fastpowered.raft.state.CurrentTerm;
import com.fastpowered.raft.state.ElectionTime;
import com.fastpowered.raft.state.Status;
import com.fastpowered.raft.state.VotedFor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Leader 选举
 *
 * 1. Leader无需选举，其它状态均需要选举
 * 2. 超时了才能选举，每个节点超时时间不能相同，Raft采用随机算法避免无谓的选举死锁
 * 3. 选举者优选选举自己，将自己变成候选人(Candidate)
 * 4. 选举的第一步就是把自己的任期号(Term)+1
 * 5. 然后像其它节点发送请求投票RPC，请求参数见论文
 * 6. 等待投票结果应该有超时控制，如果超时了，就不等待了
 * 7. 最后，如果有超过半数的响应为success，那么就立即成为领导(Leader)，并发送心跳阻止其它选举
 * 8. 如果失败了，就需要重新选举。
 *
 * 注：在选举期间，如果有其它节点发送心跳，也需要立即变成追随者(Follower)，否则，将死循环
 */
public class ElectionRunnable implements Runnable {

    private RaftThreadPool pool;
    private LogModule logModule;
    private RaftClient raftClient;

    @Override
    public void run() {
        if (Status.isLeader()) {
            return;
        }
        if (!ElectionTime.isStart()) {
            return;
        }
        Status.becomeCandidate();
        ElectionTime.update();
        CurrentTerm.increase();
        VotedFor.me();
        boolean voteResult = remoteVoteResult();
        if (Status.isFollower()) {
            return;
        }
        VotedFor.reset();
        if (voteResult) {
            Status.becomeLeader();
            Cluster.getInstance().setLeader(Cluster.getInstance().getSelf());
        }
    }

    /**
     * 远程是否过半同意选举
     * @return
     */
    private boolean remoteVoteResult() {
        List<Peer> peers = Cluster.getInstance().getPeers();
        List<Future> futures = new ArrayList<>(peers.size());
        peers.forEach(peer -> futures.add(pool.submit(()->{
            LogEntry lastLogEntry = logModule.last();
            long lastTerm = lastLogEntry != null ? lastLogEntry.getTerm() : 0L;
            RvoteParam param = new RvoteParam();
            param.setTerm(CurrentTerm.get());
            param.setCandidateId(Cluster.getInstance().getSelf().getServerId());
            param.setLastLogIndex(logModule.lastIndex());
            param.setLastLogTerm(lastTerm);
            Request request = new Request(Request.R_VOTE, param, peer.getServerId());
            Response<RvoteResult> response = raftClient.send(request);
            return response;
        })));

        AtomicInteger resultCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(futures.size());

        futures.forEach(future -> pool.submit(()->{
            try {
                Response<RvoteResult> response = (Response<RvoteResult>) future.get(3000, MILLISECONDS);
                if (response == null) {
                    return -1;
                }
                CurrentTerm.compareAndSet(response.getResult().getTerm());
                if (response.getResult().isVoteGranted()) {
                    resultCount.incrementAndGet();
                }
                return 0;
            } catch (Exception e) {
                return -1;
            } finally {
                latch.countDown();
            }
        }));

        try {
            latch.await(3500, MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (resultCount.get() >= peers.size() / 2) {
            return true;
        } else {
            return false;
        }
    }

}
