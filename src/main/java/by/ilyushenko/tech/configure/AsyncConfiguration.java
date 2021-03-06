package by.ilyushenko.tech.configure;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(1);
        executor.setThreadNamePrefix("ImportObjectThread-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                System.out.println("Throwable Exception message : " + ex.getMessage());
                System.out.println("Method name                 : " + method.getName());
                for (Object param : params) {
                    System.out.println("Parameter value             : " + param);
                }
            }
        };
    }
}
