package com.fastpowered.raft.protocol;

import com.fastpowered.raft.dto.LogEntry;

/**
 * 复制状态机
 */
public interface StateMachine {

    /**
     * 日志条目应用到状态机
     * @param logEntry
     */
    void apply(LogEntry logEntry);

}
