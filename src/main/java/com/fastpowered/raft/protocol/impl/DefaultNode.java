package com.fastpowered.raft.protocol.impl;

import com.fastpowered.raft.dto.*;
import lombok.extern.slf4j.Slf4j;

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
            log.debug("Node {} will become CANDIDATE and start election Leader, current term: {}, last entry: {}");
        }
    }


}
