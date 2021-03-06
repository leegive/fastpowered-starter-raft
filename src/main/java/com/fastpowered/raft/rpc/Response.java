package com.fastpowered.raft.rpc;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {

    private T result;

    public Response(T result) {
        this.result = result;
    }

    public static Response success() {
        return new Response("success");
    }

    public static Response failure() {
        return new Response("failure");
    }

}
