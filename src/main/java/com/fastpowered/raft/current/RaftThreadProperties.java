package com.fastpowered.raft.current;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.fastpowered.raft.current.RaftThreadPool.CPU;

@Data
@ConfigurationProperties(prefix = "fastpowered.raft.thread.pool")
public class RaftThreadProperties {

    private int maxPoolSize = CPU * 2;
    private int queueSize = 1024;
    private long keepTime = 60000;

    public RaftThreadProperties() {
        System.out.println("***********************");
    }
}
