package com.fastpowered.raft.protocol;

/**
 * 生命周期接口
 */
public interface LifeCycle {

    void init() throws Throwable;

    void destroy() throws Throwable;

}
