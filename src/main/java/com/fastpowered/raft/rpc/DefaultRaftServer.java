package com.fastpowered.raft.rpc;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.RpcServer;
import com.fastpowered.raft.dto.AentryParam;
import com.fastpowered.raft.dto.ClientRequest;
import com.fastpowered.raft.dto.RvoteParam;
import com.fastpowered.raft.protocol.Node;

import static com.fastpowered.raft.rpc.Request.*;

public class DefaultRaftServer implements RaftServer {

    private volatile boolean flag;
    private Node node;
    private RpcServer rpc;

    public DefaultRaftServer(int port, Node node) {
        if (flag) {
            return;
        }
        synchronized (this) {
            if (flag) {
                return;
            }
            rpc = new RpcServer(port, false, false);
            rpc.registerUserProcessor(new RaftUserProcessor<Request>() {
                @Override
                public Object handleRequest(BizContext bizContext, Request request) throws Exception {
                    return handlerRequest(request);
                }
            });
            this.node = node;
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

    @Override
    public Response handlerRequest(Request request) {
        switch (request.getCmd()) {
            case R_VOTE:
                return new Response(node.handlerRequestVote((RvoteParam) request.getContent()));
            case A_ENTRIES:
                return new Response(node.handlerAppendEntries((AentryParam) request.getContent()));
            case CLIENT_REQ:
                return new Response(node.handlerClientRequest((ClientRequest) request.getContent()));
            default:
                return Response.failure();
        }
    }
}
