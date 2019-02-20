package com.fastpowered.raft.protocol.state;


import com.fastpowered.raft.protocol.Cluster;
import com.fastpowered.raft.protocol.Peer;
import org.springframework.util.StringUtils;

/**
 * 我给谁投了票
 */
public final class VotedFor {

    private volatile String value;

    private VotedFor() {}

    private static class SingletonHolder {
        private static VotedFor INSTANCE = new VotedFor();
    }

    public static void set(Peer peer) {
        SingletonHolder.INSTANCE.value = peer.getServerId();
    }

    public static void me() {
        SingletonHolder.INSTANCE.value = Cluster.getInstance().getSelf().getServerId();
    }

    public static void reset() {
        SingletonHolder.INSTANCE.value = null;
    }

    public static boolean onlyCandidate(String candidateId) {
        if (StringUtils.isEmpty(SingletonHolder.INSTANCE.value) || SingletonHolder.INSTANCE.value.equals(candidateId)) {
            return true;
        } else {
            return false;
        }
    }

}
