package com.fastpowered.raft;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "fastpowered.raft")
public class RaftProperties {

    /**
     * 是否开启Raft
     */
    private boolean enable = true;

    /**
     * 如果Follower在该时间内没有从Leader收到任何消息，他将成为候选人
     */
    private long electionTimeout = 5000;

    /**
     * Leader发送心跳给Peer的周期时间，即使没有数据也要发送
     */
    private int heeartbeatPeriod = 500;



}
