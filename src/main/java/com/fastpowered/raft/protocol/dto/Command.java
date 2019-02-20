package com.fastpowered.raft.protocol.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 储存的键值对
 */
@Data
@Builder
@AllArgsConstructor
public class Command implements Serializable {

    private String key;
    private String value;

}
