package com.fastpowered.raft.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 附加日志请求结果
 */
@Data
@Builder
public class AentryResult implements Serializable {

    /**
     * 当前的任期号，用于领导人去更新自己
     */
    private long term;

    /**
     * 跟随者包含了匹配上 preLogIndex 和 preLogTerm 的日志时为真
     */
    private boolean success;

    public AentryResult() {
    }

    public AentryResult(long term) {
        this.term = term;
    }

    public AentryResult(boolean success) {
        this.success = success;
    }

    public AentryResult(long term, boolean success) {
        this.term = term;
        this.success = success;
    }

    public static AentryResult success() {
        return new AentryResult(true);
    }

    public static AentryResult failure() {
        return new AentryResult(false);
    }

}
