package com.fastpowered.raft.protocol.impl;

import com.fastpowered.raft.dto.RaftOptions;
import com.fastpowered.raft.protocol.LifeCycle;
import com.fastpowered.raft.protocol.Node;

public abstract class AbstractNode implements Node, LifeCycle {

    protected RaftOptions options;

    @Override
    public void setOptions(RaftOptions options) {
        this.options = options;
    }
}
