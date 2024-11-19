package ru.otus.hw.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

@EnableReactiveMongoRepositories(basePackages = "ru.otus.hw.repository.reactive")
@Configuration
public class ReactiveConfig {
    private static final int THREAD_POOL_SIZE = 2;

    @Bean
    public NioEventLoopGroup eventLoopGroup() {
        return new NioEventLoopGroup(THREAD_POOL_SIZE,
                new ThreadFactory() {
                    private final AtomicLong threadIdGenerator = new AtomicLong(0);
                    @Override
                    public Thread newThread(@NonNull Runnable task) {
                        return new Thread(task, "server-thread-" + threadIdGenerator.incrementAndGet());
                    }
                });
    }

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory(NioEventLoopGroup eventLoopGroup) {
        var factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(builder -> builder.runOn(eventLoopGroup));
        return factory;
    }

    @Bean
    public Scheduler workerPool() {
        return Schedulers.newParallel("worker-thread", THREAD_POOL_SIZE);
    }
}
