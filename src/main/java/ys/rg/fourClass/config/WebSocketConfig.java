package ys.rg.fourClass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket核心配置类：开启WebSocket支持
 */
@Configuration
public class WebSocketConfig {
    // 注册WebSocket端点处理器，让Spring管理WebSocket连接
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
