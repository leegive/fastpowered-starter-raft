package com.fastpowered.raft.rpc;

public interface RaftClient {

    Response send(Request request);

}
