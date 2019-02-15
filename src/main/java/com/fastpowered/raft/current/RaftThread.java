package com.fastpowered.raft.current;

import lombok.extern.slf4j.Slf4j;

/**
 * Raft 定制线程
 *
 * 进行捕获异常，并输出日志
 */
@Slf4j
public class RaftThread extends Thread {

    private static final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (t, e)
    -> log.warn("Exception occurred from RaftThread {}", t.getName(), e);

    public RaftThread(String threadName, Runnable runnable) {
        super(runnable, threadName);
        setUncaughtExceptionHandler(uncaughtExceptionHandler);
    }
}
