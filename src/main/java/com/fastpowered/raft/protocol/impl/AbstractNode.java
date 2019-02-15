package com.fastpowered.raft.protocol.impl;

import com.fastpowered.raft.dto.RaftOptions;
import com.fastpowered.raft.protocol.LifeCycle;
import com.fastpowered.raft.protocol.Node;
import com.fastpowered.raft.protocol.NodeStatus;

public abstract class AbstractNode implements Node, LifeCycle {

    /**
     * Node默认状态
     */
    protected int status = NodeStatus.FOLLOWER;

    /**
     * 选举时间间隔基数
     */
    protected volatile long electionTime = 15 * 1000;

    /**
     * 心跳时间间隔基数
     */
    protected long heartBeatTick = 5 * 1000;

    /**
     * 上次选举时间
     */
    protected volatile long preElectionTime = 0;

    /**
     * 上次心跳时间
     */
    protected volatile long preHeartBeatTime = 0;

    @Override
    public void setOptions(RaftOptions options) {
        this.electionTime = options.getElectionTime();
        this.heartBeatTick = options.getHeartBeatTick();
    }

    @Override
    public void init() throws Throwable {

    }

    @Override
    public void destroy() throws Throwable {

    }
}
