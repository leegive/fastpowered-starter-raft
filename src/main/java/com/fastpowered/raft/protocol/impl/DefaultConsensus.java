package com.fastpowered.raft.protocol.impl;

import com.fastpowered.raft.dto.*;
import com.fastpowered.raft.protocol.Consensus;
import com.fastpowered.raft.state.CurrentTerm;
import com.fastpowered.raft.state.VotedFor;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

import static com.fastpowered.raft.protocol.NodeStatus.FOLLOWER;

public class DefaultConsensus implements Consensus {

    private final DefaultNode node;
    private final ReentrantLock voteLock = new ReentrantLock();
    private final ReentrantLock appendLock = new ReentrantLock();

    public DefaultConsensus(DefaultNode node) {
        this.node = node;
    }

    @Override
    public RvoteResult requestVote(RvoteParam param) {
        try {
            RvoteResult result = new RvoteResult();
            if (!voteLock.tryLock()) {
                result.setVoteGranted(false);
                result.setTerm(CurrentTerm.get());
                return result;
            }

            if (param.getTerm() < CurrentTerm.get()) {
                result.setVoteGranted(false);
                result.setTerm(CurrentTerm.get());
                return result;
            }
            if (VotedFor.onlyCandidate(param.getCandidateId())) {
                if (node.logModule.last() != null) {
                    if (node.logModule.last().getTerm() > param.getLastLogTerm()) {
                        return RvoteResult.failure();
                    }
                    if (node.logModule.lastIndex() > param.getLastLogIndex()) {
                        return RvoteResult.failure();
                    }
                }
                node.status = FOLLOWER;
                node.cluster.setLeader(new Peer(param.getCandidateId()));
                CurrentTerm.set(param.getTerm());
                VotedFor.set(new Peer(param.getCandidateId()));
                result.setVoteGranted(true);
                result.setTerm(CurrentTerm.get());
                return result;
            } else {
                result.setVoteGranted(false);
                result.setTerm(CurrentTerm.get());
                return result;
            }
        } finally {
            voteLock.unlock();
        }
    }

    @Override
    public AentryResult appendEntries(AentryParam param) {
        AentryResult result = AentryResult.failure();
        try {
            if (!appendLock.tryLock()) {
                return result;
            }
            result.setTerm(CurrentTerm.get());
            if (param.getTerm() < CurrentTerm.get()) {
                return result;
            }
            node.preHeartBeatTime = System.currentTimeMillis();
            node.preElectionTime = System.currentTimeMillis();
            node.cluster.setLeader(new Peer(param.getLeaderId()));

            if (param.getTerm() >= CurrentTerm.get()) {
                node.status = FOLLOWER;
            }

            CurrentTerm.set(param.getTerm());
            if (param.getEntries() == null || param.getEntries().length == 0) {
                result.setTerm(CurrentTerm.get());
                result.setSuccess(true);
                return result;
            }

            if (node.logModule.lastIndex() != 0 && param.getPreLogIndex() != 0) {
                LogEntry entry;
                if ((entry = node.logModule.get(param.getPreLogIndex())) != null) {
                    if (entry.getTerm() != param.getPreLogIndex()) {
                        return result;
                    }
                } else {
                    return result;
                }
            }

            LogEntry existLog = node.logModule.get(param.getPreLogIndex() + 1);
            if (existLog != null && existLog.getTerm() != param.getEntries()[0].getTerm()) {
                node.logModule.remove(param.getPreLogIndex() + 1);
            } else if (existLog != null) {
                result.setSuccess(true);
                return result;
            }

            Arrays.stream(param.getEntries()).forEach(logEntry -> {
                node.logModule.put(logEntry);
                node.stateMachine.apply(logEntry);
                result.setSuccess(true);
            });

            if (param.getLeaderCommit() > node.commitIndex) {
                long commitIndex = Math.min(param.getLeaderCommit(), node.logModule.lastIndex());
                node.commitIndex = commitIndex;
                node.lastApplied = commitIndex;
            }

            result.setTerm(CurrentTerm.get());
            return result;
        } finally {
            appendLock.unlock();
        }
    }
}
