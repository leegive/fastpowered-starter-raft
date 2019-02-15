package com.fastpowered.raft.config;

import com.fastpowered.raft.current.RaftThreadPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "fastpowered.raft", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({
    RaftThreadProperties.class,
    RaftProperties.class
})
public class RaftAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RaftThreadPool raftThreadPool(RaftThreadProperties properties) {
        return new RaftThreadPool(properties);
    }

}
