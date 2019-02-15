package com.fastpowered.raft;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

@Configuration
@ConditionalOnProperty(prefix = "fastpowered.raft", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(RaftProperties.class)
public class RaftAutoConfiguration implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }


//    @Bean
//    public ThreadPoolTaskExecutor raftThreadPoolTaskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(4);
//        executor.setMaxPoolSize(8);
//        executor.setQueueCapacity(100);
//        executor.setKeepAliveSeconds(60);
//        executor.setThreadNamePrefix("Raft-thread");
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.initialize();
//        return executor;
//    }


}
