package tn.enova.Configures;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {
    @Bean(name = "mail-smtp")
    public ThreadPoolTaskExecutor taskExecutorMail() {//Executor
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
    @Bean(name = "web-client")
    public ThreadPoolTaskExecutor taskExecutorWebClient() {//Executor
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the core pool size to handle 50 users
        executor.setMaxPoolSize(20); // Set the max pool size to allow for additional threads if needed
        executor.setQueueCapacity(100); // Set the queue capacity to handle incoming tasks
        executor.setThreadNamePrefix("WEB-CLIENT - sending-multi-body-content");
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn("WEB-CLIENT rejected, thread pool is full and queue is also full"));
        executor.setThreadNamePrefix("WEB-CLIENT-EXECUTOR"); // Set a thread name prefix
        executor.initialize();
        return executor;
    }
}
