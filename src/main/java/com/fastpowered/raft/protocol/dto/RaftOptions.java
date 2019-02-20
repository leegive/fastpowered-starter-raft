package com.fastpowered.raft.protocol.dto;

import lombok.Data;

@Data
public class RaftOptions {

    private long electionTime;

    private long heartBeatTick;

}
