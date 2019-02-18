package com.fastpowered.raft.protocol;

import lombok.Data;

import java.util.Objects;

@Data
public class Peer {

    private String serverId;

    private long nextIndexs = 0;
    private long matchIndexs = 0;

    public Peer(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peer peer = (Peer) o;
        return Objects.equals(serverId, peer.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId);
    }
}
