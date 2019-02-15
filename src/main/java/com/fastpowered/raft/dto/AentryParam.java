package com.fastpowered.raft.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 附加日志请求参数
 */
@Data
@Builder
public class AentryParam extends BaseParam {

    /**
     * 领导人的 Id，以便于跟随者重定向请求
     */
    private String leaderId;

    /**
     * 领导人已经提交的日志的索引值
     */
    private long leaderCommit;

    /**
     * 新的日志条目紧随之前的索引值
     */
    private long preLogIndex;

    /**
     * preLogIndex 条目的任期号
     */
    private long preLogTerm;

    /**
     * 准备存储的日志条目（表示心跳时为空；一次性发送多个是为了提高效率）
     */
    private LogEntry[] entries;

}
