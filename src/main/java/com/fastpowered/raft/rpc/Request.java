package com.fastpowered.raft.rpc;

import com.fastpowered.raft.protocol.dto.AentryParam;
import com.fastpowered.raft.protocol.dto.ClientRequest;
import com.fastpowered.raft.protocol.dto.RvoteParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class Request<T> implements Serializable {

    /** 请求投票 */
    public static final int R_VOTE = 0;
    /** 附加日志 */
    public static final int A_ENTRIES = 1;
    /** 客户端 */
    public static final int CLIENT_REQ = 2;
    /** 配置变更. add*/
    public static final int CHANGE_CONFIG_ADD = 3;
    /** 配置变更. remove*/
    public static final int CHANGE_CONFIG_REMOVE = 4;

    /** 请求类型 */
    private int cmd = -1;

    /**
     * @see AentryParam
     * @see RvoteParam
     * @see ClientRequest
     */
    private T content;

    private String url;

    public Request() {
    }

    public Request(T content) {
        this.content = content;
    }

    public Request(int cmd, T content, String url) {
        this.cmd = cmd;
        this.content = content;
        this.url = url;
    }
}
