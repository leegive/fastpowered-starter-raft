package com.fastpowered.raft.current;

import lombok.Data;

import static com.fastpowered.raft.current.RaftThreadPool.CPU;

@Data
public class RaftThreadProperties {

    private int maxPoolSize = CPU * 2;
    private int queueSize = 1024;
    private long keepTime = 60000;

}
