package com.enova.web.api.Configures;

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
    @Bean(name = "mail-smtp")
    public Executor taskExecutorMail() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30); // Set the core pool size to handle 50 users
        executor.setMaxPoolSize(60); // Set the max pool size to allow for additional threads if needed
        executor.setQueueCapacity(600); // Set the queue capacity to handle incoming tasks
        executor.setThreadNamePrefix("Mail-SMTP-Service - sending-multi-body-content");
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn("sendingMultiBodyContent rejected, thread pool is full and queue is also full"));
        executor.setThreadNamePrefix("Mail-SMTP-Executor-"); // Set a thread name prefix
        executor.initialize();
        return executor;
    }

    @Bean(name = "get-robot-data")
    public Executor taskExecutorDataRobot() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50); // Set the core pool size to handle 50 users
        executor.setMaxPoolSize(100); // Set the max pool size to allow for additional threads if needed
        executor.setQueueCapacity(1000); // Set the queue capacity to handle incoming tasks
        executor.setThreadNamePrefix("robot-service - get-robot-data");
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn(" get-robot-data rejected, thread pool is full and queue is also full"));
        executor.setThreadNamePrefix("get-robot-data-Executor-"); // Set a thread name prefix
        executor.initialize();
        return executor;
    }
}
