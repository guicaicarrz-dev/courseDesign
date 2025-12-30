package ys.rg.fourClass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import ys.rg.fourClass.handler.AlgorithmWebSocketHandler;

import javax.annotation.Resource;

/**
 * WebSocket核心配置类：开启WebSocket支持
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private AlgorithmWebSocketHandler algorithmWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册WebSocket端点，允许前端跨域连接
        registry.addHandler(algorithmWebSocketHandler, "/algorithm/ws/{experimentId}")
                .setAllowedOrigins("http://localhost:8081"); // 替换为你的前端域名
    }
}
