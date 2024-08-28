package com.enova.notification.api.Configures;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {

    @Bean(name = "mqtt-threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor mqttExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the core pool size
        executor.setMaxPoolSize(100); // Set the maximum pool size
        executor.setQueueCapacity(500); // Set the queue capacity
        executor.setThreadNamePrefix("MQTT-"); // Set the thread name prefix
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // Set the rejected execution handler
        executor.initialize();
        return executor;
    }
    @Bean
    public ExecutorService mqttExecutorService(@Qualifier("mqtt-threadPoolTaskExecutor") ThreadPoolTaskExecutor executor) {
        return executor.getThreadPoolExecutor();
    }
}