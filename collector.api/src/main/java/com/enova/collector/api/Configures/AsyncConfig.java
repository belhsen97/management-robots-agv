package com.enova.collector.api.Configures;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50); // Set the core pool size to handle 50 robots
        executor.setMaxPoolSize(100); // Set the max pool size to allow for additional threads if needed
        executor.setQueueCapacity(1000); // Set the queue capacity to handle incoming tasks
        executor.setThreadNamePrefix("CollectorCalback - MessageArrived");
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn("messageArrived rejected, thread pool is full and queue is also full"));
        executor.setThreadNamePrefix("RobotExecutor-"); // Set a thread name prefix
        executor.initialize();
        return executor;
    }
}
