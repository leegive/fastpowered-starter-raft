package com.fastpowered.raft.current;

import java.util.concurrent.ThreadFactory;

/**
 * @author li.shangjin@icloud.com
 */
public class RaftThreadFactory implements ThreadFactory {

    public static final String RAFT_THREAD_PREFIX = "raft-thread-";

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new RaftThread(runnable, RAFT_THREAD_PREFIX);
        thread.setDaemon(true);
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
    }

}
