package com.fastpowered.raft.rpc;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.RpcServer;

public class DefaultRaftServer implements RaftServer {

    private volatile boolean flag;
    private RpcServer rpc;
    private HandlerRequest handlerRequest;

    public DefaultRaftServer(int port, HandlerRequest handlerRequest) {
        if (flag) {
            return;
        }
        synchronized (this) {
            if (flag) {
                return;
            }
            this.handlerRequest = handlerRequest;
            rpc = new RpcServer(port, false, false);
            rpc.registerUserProcessor(new RaftUserProcessor<Request>() {
                @Override
                public Object handleRequest(BizContext bizContext, Request request) throws Exception {
                    return handlerRequest.exec(request);
                }
            });
            flag = true;
        }
    }

    @Override
    public void start() {
        this.rpc.start();
    }

    @Override
    public void stop() {
        this.rpc.stop();
    }

}
