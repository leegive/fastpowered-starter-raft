package com.fastpowered.raft.protocol;

import com.fastpowered.raft.dto.LogEntry;

/**
 * 日志模块
 */
public interface LogModule {

    void put(LogEntry entry);

    LogEntry get(Long index);

    void remove(Long startIndex);

    LogEntry last();

    Long lastIndex();

}
