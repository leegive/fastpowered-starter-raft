package com.fastpowered.raft.state;

/**
 * 节点状态
 */
public final class Status {

    private volatile int value = 0;

    private Status() {}

    private static class SingletonHolder {
        private static Status INSTANCE = new Status();
    }

    public static void becomeFollower() {
        SingletonHolder.INSTANCE.value = 0;
    }

    public static void becomeCandidate() {
        SingletonHolder.INSTANCE.value = 1;
    }

    public static void becomeLeader() {
        SingletonHolder.INSTANCE.value = 2;
    }

    public static boolean isFollower() {
        return SingletonHolder.INSTANCE.value == 0;
    }

    public static boolean isCandidate() {
        return SingletonHolder.INSTANCE.value == 1;
    }

    public static boolean isLeader() {
        return SingletonHolder.INSTANCE.value == 2;
    }

    public static int get() {
        return SingletonHolder.INSTANCE.value;
    }

    public static String getDescribe() {
        switch (SingletonHolder.INSTANCE.value) {
            case 0:
                return "Follower";
            case 1:
                return "Candidate";
            case 2:
                return "Leader";
            default:
                throw new IllegalStateException("Raft node status illegal value");
        }
    }

}
