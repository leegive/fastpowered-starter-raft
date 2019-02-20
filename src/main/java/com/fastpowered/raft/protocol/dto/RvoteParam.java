package com.fastpowered.raft.protocol.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 候选人请求投票参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RvoteParam extends BaseParam {

    /**
     * 候选人ID
     * 样例: ip:port
     */
    private String candidateId;

    /**
     * 候选人最后日志条目的索引值
     */
    private long lastLogIndex;

    /**
     * 候选人最后日志条目的任期号
     */
    private long lastLogTerm;

}
