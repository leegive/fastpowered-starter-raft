package com.fastpowered.raft.protocol.impl;

import com.fastpowered.raft.dto.LogEntry;
import com.fastpowered.raft.protocol.LogModule;

public class DefaultLogModule implements LogModule {
    @Override
    public void put(LogEntry entry) {

    }

    @Override
    public LogEntry get(Long index) {
        return null;
    }

    @Override
    public void remove(Long startIndex) {

    }

    @Override
    public LogEntry last() {
        return null;
    }

    @Override
    public Long lastIndex() {
        return null;
    }
}
