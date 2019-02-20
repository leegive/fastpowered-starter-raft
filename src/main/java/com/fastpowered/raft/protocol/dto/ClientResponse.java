package com.fastpowered.raft.protocol.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientResponse<T> implements Serializable {

    private T result;

    public static ClientResponse success() {
        ClientResponse<String> response = new ClientResponse<>();
        response.setResult("success");
        return response;
    }

    public static ClientResponse failure() {
        ClientResponse<String> response = new ClientResponse<>();
        response.setResult("failure");
        return response;
    }

}
