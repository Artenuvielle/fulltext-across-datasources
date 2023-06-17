package de.martinclan.fulltextacrossdatasources;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Set the desired number of threads
        executor.setMaxPoolSize(10); // Set the maximum number of threads
        executor.setQueueCapacity(25); // Set the queue capacity
        executor.setThreadNamePrefix("BackgroundThread-");
        executor.initialize();
        return executor;
    }
}
