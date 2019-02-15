package com.fastpowered.raft.protocol.impl;

import com.fastpowered.raft.dto.RaftOptions;
import com.fastpowered.raft.protocol.LifeCycle;
import com.fastpowered.raft.protocol.Node;

public abstract class AbstractNode implements Node, LifeCycle {

    /**
     * 选举时间间隔基数
     */
    private volatile long electionTime = 15 * 1000;

    /**
     * 心跳时间间隔基数
     */
    private long heartBeatTick = 5 * 1000;

    /**
     * 上次选举时间
     */
    private volatile long preElectionTime = 0;

    /**
     * 上次心跳时间
     */
    private volatile long preHeartBeatTime = 0;

    @Override
    public void setOptions(RaftOptions options) {
        this.electionTime = options.getElectionTime();
        this.heartBeatTick = options.getHeartBeatTick();
    }
}
