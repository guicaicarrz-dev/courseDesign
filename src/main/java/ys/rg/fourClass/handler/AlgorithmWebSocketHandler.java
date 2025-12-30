package ys.rg.fourClass.handler;

import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ys.rg.fourClass.service.AlgorithmService;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能：1. 接收前端WebSocket指令 2. 推送算法进度给前端
 * 用法：被WebSocketConfig注册，前端连接后指令都走这里，转发给AlgorithmService处理
 */
@Component
public class AlgorithmWebSocketHandler extends TextWebSocketHandler {

    // 缓存WebSocket会话：key=experimentId，value=会话
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Resource
    private AlgorithmService algorithmService;

    // 指令DTO（前端传参格式）
    public static class AlgorithmCommand {
        private String experimentId; // 实验ID
        private String algorithmType; // 算法类型（FIFO/LRU/OPT）
        private String command; // 指令：START/PAUSE/RESUME
        // getter/setter
        public String getExperimentId() { return experimentId; }
        public void setExperimentId(String experimentId) { this.experimentId = experimentId; }
        public String getAlgorithmType() { return algorithmType; }
        public void setAlgorithmType(String algorithmType) { this.algorithmType = algorithmType; }
        public String getCommand() { return command; }
        public void setCommand(String command) { this.command = command; }
    }

    // 建立连接时缓存会话
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) {
            return;
        }
        // 示例URI：/algorithm/ws/123 → 分割后取最后一段作为experimentId
        String path = uri.getPath();
        String[] pathParts = path.split("/");
        String experimentId = pathParts[pathParts.length - 1]; // 取路径最后一部分
        sessionMap.put(experimentId, session);
    }

    // 接收前端指令，转发给AlgorithmService
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        AlgorithmCommand command = JSON.parseObject(message.getPayload(), AlgorithmCommand.class);
        switch (command.getCommand()) {
            case "START":
                algorithmService.startAlgorithm(Integer.parseInt(command.getExperimentId()), command.getAlgorithmType());
                break;
            case "PAUSE":
                algorithmService.pauseAlgorithm(Integer.parseInt(command.getExperimentId()), command.getAlgorithmType());
                break;
            case "RESUME":
                algorithmService.resumeAlgorithm(Integer.parseInt(command.getExperimentId()), command.getAlgorithmType());
                break;
        }
    }

    // 向前端推送进度（AlgorithmService/Task调用此方法）
    public void sendMessage(String experimentId, String jsonMsg) {
        WebSocketSession session = sessionMap.get(experimentId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(jsonMsg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 关闭连接时清理会话
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        URI uri = session.getUri();
        if (uri == null) {
            return;
        }
        String path = uri.getPath();
        String[] pathParts = path.split("/");
        String experimentId = pathParts[pathParts.length - 1];
        sessionMap.remove(experimentId);
        algorithmService.stopAllAlgorithms(Integer.parseInt(experimentId));
    }
}
