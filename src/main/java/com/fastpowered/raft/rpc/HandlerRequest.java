package com.fastpowered.raft.rpc;

public interface HandlerRequest {

    Response exec(Request request);

}
