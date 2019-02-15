package com.fastpowered.raft.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 日志条目
 */
@Data
@Builder
public class LogEntry implements Comparable, Serializable {

    private Long index;
    private long term;
    private Command command;

    public LogEntry() {
    }

    public LogEntry(long term, Command command) {
        this.term = term;
        this.command = command;
    }

    public LogEntry(Long index, long term, Command command) {
        this.index = index;
        this.term = term;
        this.command = command;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return -1;
        }
        return this.getIndex().compareTo(((LogEntry) o).getIndex());
    }
}
