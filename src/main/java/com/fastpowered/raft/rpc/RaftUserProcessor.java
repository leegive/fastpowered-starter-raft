package com.fastpowered.raft.rpc;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AbstractUserProcessor;

public abstract class RaftUserProcessor<T> extends AbstractUserProcessor<T> {

    @Override
    public void handleRequest(BizContext bizContext, AsyncContext asyncContext, T request) {
        throw new UnsupportedClassVersionError("Raft Server not support handleRequest");
    }

    @Override
    public String interest() {
        return Request.class.getName();
    }
}
