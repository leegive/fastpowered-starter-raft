package com.fastpowered.raft.protocol.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 候选人请求投票结果
 */
@Data
public class RvoteResult implements Serializable {

    /**
     * 投票人当前任期号，以便于候选人去更新自己的任期号
     */
    private long term;

    /**
     * 投票给候选人时，此选票为True
     */
    private boolean voteGranted;

    public RvoteResult() {
    }

    public RvoteResult(boolean voteGranted) {
        this.voteGranted = voteGranted;
    }

    public static RvoteResult success() {
        return new RvoteResult(true);
    }

    public static RvoteResult failure() {
        return new RvoteResult(false);
    }

}
