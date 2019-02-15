package com.fastpowered.raft.dto;

import lombok.Data;

@Data
public class RaftOptions {

    private long electionTime;

    private long heartBeatTick;

}
