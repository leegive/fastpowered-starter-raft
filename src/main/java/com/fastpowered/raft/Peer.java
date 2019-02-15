package com.fastpowered.raft;

import lombok.Data;

/**
 * 集群节点
 */
@Data
public class Peer {

    /**
     * 节点ID,由IP:PORT组成
     * 例{192.168.1.10:8080}
     */
    private String serviceId;

    /**
     * 需要发送给follower的下一个日志条目的索引值，只对leader有效
     */
    private long nextIndex;

    /**
     * 已复制日志的最高索引值
     */
    private long matchIndex;

    /**
     * True表示给Candidate投票
     */
    private Boolean voteGranted;

}
