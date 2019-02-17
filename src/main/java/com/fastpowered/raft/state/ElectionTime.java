package com.fastpowered.raft.state;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 选举时间
 */
public final class ElectionTime {

    private volatile long timeout = 15000;
    private volatile long preTime = 0;


    private ElectionTime() {}

    private static class SingletonHolder {
        private static ElectionTime INSTANCE = new ElectionTime();
    }

    public static long getTimeout() {
        return SingletonHolder.INSTANCE.timeout += ThreadLocalRandom.current().nextInt(500);
    }

    public static boolean isStart() {
        if (System.currentTimeMillis() - SingletonHolder.INSTANCE.preTime < getTimeout()) {
            return false;
        } else {
            return true;
        }
    }

    public static void update() {
        SingletonHolder.INSTANCE.preTime = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200) + 150;
    }

}
