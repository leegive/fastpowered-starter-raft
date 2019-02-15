package com.fastpowered.raft.protocol;

import com.fastpowered.raft.dto.*;

/**
 * 服务节点
 */
public interface Node {

    /**
     * 算法配置选项信息
     * @param options
     */
    void setOptions(RaftOptions options);

    /**
     * 处理投票请求
     * @param param
     * @return
     */
    RvoteResult handlerRequestVote(RvoteParam param);

    /**
     * 处理附加日志请求
     * @param param
     * @return
     */
    AentryResult handlerAppendEntries(AentryParam param);

    /**
     * 处理客户端请求
     * @param request
     * @return
     */
    ClientResponse handlerClientRequest(ClientRequest request);

    /**
     * 转发客户端请求给Leader
     * @param request
     * @return
     */
    ClientResponse redirect(ClientRequest request);

}
