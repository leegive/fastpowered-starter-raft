package com.fastpowered.raft.state;

/**
 * 最后一次知道的任期号，初始化为0，持续递增
 */
public final class CurrentTerm {

    private volatile long value = 0;

    private CurrentTerm() {}

    private static class SingletonHolder {
        private static CurrentTerm INSTANCE = new CurrentTerm();
    }

    public static void set(long value) {
        SingletonHolder.INSTANCE.value = value;
    }

    public static long get() {
        return SingletonHolder.INSTANCE.value;
    }

    public static void increase() {
        SingletonHolder.INSTANCE.value++;
    }

    public static void compareAndSet(long value) {
        if (SingletonHolder.INSTANCE.value < value) {
            SingletonHolder.INSTANCE.value = value;
        }
    }

    public static void compareAndSet(long value, Runnable runnable) {
        if (SingletonHolder.INSTANCE.value < value) {
            SingletonHolder.INSTANCE.value = value;
            runnable.run();
        }
    }

}
