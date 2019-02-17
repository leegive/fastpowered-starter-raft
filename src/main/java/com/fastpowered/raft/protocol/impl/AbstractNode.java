package com.fastpowered.raft.protocol.impl;

import com.fastpowered.raft.current.RaftThreadPool;
import com.fastpowered.raft.dto.RaftOptions;
import com.fastpowered.raft.protocol.*;
import com.fastpowered.raft.rpc.RaftClient;
import lombok.Data;

import java.util.Map;

@Data
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

    /**
     * 已知的最大的已经被提交的日志条目的索引值
     */
    protected volatile long commitIndex;

    /**
     * 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增)
     */
    volatile long lastApplied = 0;

    protected Cluster cluster = Cluster.getInstance();

    protected LogModule logModule;

    protected RaftThreadPool threadPool;

    protected RaftClient raftClient;

    /**
     *  对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一）
     */
    protected Map<Peer, Long> nextIndexs;

    /**
     * 对于每一个服务器，已经复制给他的日志的最高索引值
     */
    protected Map<Peer, Long> matchIndexs;

    protected StateMachine stateMachine;

    @Override
    public void setOptions(RaftOptions options) {
        this.electionTime = options.getElectionTime();
        this.heartBeatTick = options.getHeartBeatTick();
        logModule = new DefaultLogModule();
    }

    @Override
    public void init() throws Throwable {

    }

    @Override
    public void destroy() throws Throwable {

    }
}
