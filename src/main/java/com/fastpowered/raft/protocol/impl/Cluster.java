package com.fastpowered.raft.protocol.impl;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cluster {

    private volatile Peer self;
    private volatile Peer leader;
    private List<Peer> peers = new ArrayList<>();

    private Cluster(){}

    public static Cluster getInstance() {
        return ClusterLazyHolder.INSTANCE;
    }

    private static class ClusterLazyHolder {
        private static final Cluster INSTANCE = new Cluster();
    }

}
