package com.fastpowered.raft.protocol;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cluster {

    private volatile Peer self;
    private volatile Peer leader;
    private List<Peer> peers = new ArrayList<>();
    private LogModule logModule;

    private Cluster(){}

    public static Cluster getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final Cluster INSTANCE = new Cluster();
    }

    public static void addPeer(String serverId) {
        Peer peer = new Peer(serverId);
        peer.setNextIndexs(SingletonHolder.INSTANCE.logModule.getLastIndex() + 1);
        peer.setMatchIndexs(0L);
        SingletonHolder.INSTANCE.peers.add(peer);
    }

}
