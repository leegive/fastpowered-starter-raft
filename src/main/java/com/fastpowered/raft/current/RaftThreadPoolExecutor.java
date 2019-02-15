package com.fastpowered.raft.current;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 主要计算线程消耗时间
 */
@Slf4j
public class RaftThreadPoolExecutor extends ThreadPoolExecutor {

    private static final ThreadLocal<Long> COST_TIME_WATCH = ThreadLocal.withInitial(System::currentTimeMillis);

    public RaftThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RaftThreadFactory factory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, factory);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        COST_TIME_WATCH.get();
        log.debug("RaftThread pool before Execute");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        log.debug("RaftThread pool after Execute, cost time : {}", System.currentTimeMillis() - COST_TIME_WATCH.get());
        COST_TIME_WATCH.remove();
    }

    @Override
    protected void terminated() {
        log.info("Active count : {}, queue size : {}, pool size : {}", getActiveCount(), getQueue().size(), getPoolSize());
    }
}
