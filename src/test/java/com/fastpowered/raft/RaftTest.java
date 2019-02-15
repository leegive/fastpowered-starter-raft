package com.fastpowered.raft;

import com.fastpowered.raft.current.RaftThreadProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootApplication
@ComponentScan(basePackages = {"com.fastpowered.raft"})
public class RaftTest {

    @Autowired
    private RaftThreadProperties properties;

    @Test
    public void testProperties() {
        System.out.println(properties.getQueueSize());
    }

}
