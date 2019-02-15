package com.fastpowered.raft.rpc;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultRaftClient implements RaftClient {

    private final static RpcClient CLIENT = new RpcClient();
    static {
        CLIENT.init();
    }

    @Override
    public Response send(Request request) {
        Response response = null;
        try {
            response = (Response) CLIENT.invokeSync(request.getUrl(), request, 200000);
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}
