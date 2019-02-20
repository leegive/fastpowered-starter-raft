package com.fastpowered.raft.protocol.dto;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * 客户端请求
 */
@Data
public class ClientRequest implements Serializable {

    private Method method;
    private String key;
    private String value;

    public static ClientRequest get(@NonNull String key) {
        ClientRequest request = new ClientRequest();
        request.setMethod(Method.GET);
        request.setKey(key);
        return request;
    }

    public static ClientRequest put(@NonNull String key, @NonNull String value) {
        ClientRequest request = new ClientRequest();
        request.setMethod(Method.PUT);
        request.setKey(key);
        request.setValue(value);
        return request;
    }

    public enum Method {
        PUT,GET
    }

}
