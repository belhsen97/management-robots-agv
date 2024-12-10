package tn.enova.Configures;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {
    @Bean(name = "mqtt-thread-pool-task-executor")
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
    public ExecutorService mqttExecutorService(@Qualifier("mqtt-thread-pool-task-executor") ThreadPoolTaskExecutor executor) {
        return executor.getThreadPoolExecutor();
    }
}
