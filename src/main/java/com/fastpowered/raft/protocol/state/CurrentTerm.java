package com.fastpowered.raft.protocol.state;

/**
 * 最后一次知道的任期号，初始化为0，持续递增
 */
public final class CurrentTerm {

    private volatile long value = 0;

    private static CurrentTerm INSTANCE = new CurrentTerm();

    private CurrentTerm() {}

    public static void set(long value) {
        INSTANCE.value = value;
    }

    public static long get() {
        return INSTANCE.value;
    }

    public static void increase() {
        INSTANCE.value++;
    }

    public static void compareAndSet(long value) {
        if (INSTANCE.value < value) {
            INSTANCE.value = value;
        }
    }

    public static void compareAndSet(long value, Runnable runnable) {
        if (INSTANCE.value < value) {
            INSTANCE.value = value;
            runnable.run();
        }
    }

}
