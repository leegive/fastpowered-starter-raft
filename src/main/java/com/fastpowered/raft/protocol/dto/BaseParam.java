package com.fastpowered.raft.protocol.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class BaseParam implements Serializable {

    /**
     * 候选人的任期号
     */
    private long term;

    /**
     * 被请求的ServerID
     * 样例: ip:port
     */
    private String serverId;

}
