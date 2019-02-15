package com.fastpowered.raft.rpc;

public interface RaftServer {

    void start();

    void stop();

    Response handlerRequest(Request request);

}
