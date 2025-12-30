package ys.rg.fourClass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;

/**
 * 功能：创建算法专用线程池，适配多算法并行执行
 * 用法：注入到AlgorithmService中，调用submit()提交PageReplacementTask任务
 */
@Configuration
public class AlgorithmThreadPoolConfig {

    @Bean(name = "algorithmExecutor")
    public ExecutorService algorithmExecutor() {
        int core = Runtime.getRuntime().availableProcessors() * 2; // 8核CPU→16核心线程
        int max = Runtime.getRuntime().availableProcessors() * 4;
        return new ThreadPoolExecutor(
                core,
                max,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> new Thread(r, "Algorithm-Thread-" + System.currentTimeMillis()),
                new ThreadPoolExecutor.CallerRunsPolicy() // 任务拒绝时主线程兜底
        );
    }
}
