package com.salmalteam.salmal.fcm.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setBeanName("FCM");
		threadPoolTaskExecutor.setCorePoolSize(1);
		threadPoolTaskExecutor.setMaxPoolSize(100);
		threadPoolTaskExecutor.setThreadNamePrefix("FCM-Executor-");
		threadPoolTaskExecutor.setRejectedExecutionHandler((r, executor) -> {
			throw new IllegalArgumentException("thread pool 에서 더이상 요청을 처리할 수 없습니다.");
		});
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}
}
