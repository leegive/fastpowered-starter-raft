package com.fastpowered.raft.current;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class RaftThreadPool {

    public static int CPU = Runtime.getRuntime().availableProcessors();
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private int maxPoolSize;
    private int queueSize;
    private long keepTime;

    private ScheduledExecutorService ses = getScheduled();
    private ThreadPoolExecutor tpe = getThreadPool();

    public RaftThreadPool(RaftThreadProperties properties) {
        log.info("Initializing RaftThreadPool");
        this.maxPoolSize = properties.getMaxPoolSize();
        this.queueSize = properties.getQueueSize();
        this.keepTime = properties.getKeepTime();
    }

    public void scheduleAtFixedRate(Runnable runnable, long initDelay, long delay) {
        ses.scheduleAtFixedRate(runnable, initDelay, delay, TIME_UNIT);
    }

    public void scheduleWithFixedDelay(Runnable runnable, long delay) {
        ses.scheduleWithFixedDelay(runnable, 0, delay, TIME_UNIT);
    }

    public <T> Future<T> submit(Callable callable) {
        return tpe.submit(callable);
    }

    public void execute(Runnable runnable) {
        tpe.execute(runnable);
    }

    public void execute(Runnable runnable, boolean sync) {
        if (sync) {
            runnable.run();
        } else {
            tpe.execute(runnable);
        }
    }

    private ScheduledExecutorService getScheduled() {
        return new ScheduledThreadPoolExecutor(CPU, new RaftThreadFactory());
    }

    private ThreadPoolExecutor getThreadPool() {
        return new RaftThreadPoolExecutor(CPU,maxPoolSize,keepTime,TIME_UNIT,new LinkedBlockingQueue<>(queueSize),new RaftThreadFactory());
    }


}
