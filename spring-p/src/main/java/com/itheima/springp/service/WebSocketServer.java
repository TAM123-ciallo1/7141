package com.itheima.springp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.springp.config.ApplicationContextProvider;
import com.itheima.springp.event.MqttMessageEvent;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{openid}")
public class WebSocketServer implements WebSocketHandler, ApplicationListener<MqttMessageEvent> {
    @Autowired
    @Lazy
    private MqttService mqttService;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Autowired
    private DeviceUserService deviceUserService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String openid = (String) session.getAttributes().get("openid");
        if (openid == null) {
            session.close(CloseStatus.PROTOCOL_ERROR);
            return;
        }
        sessionMap.put(openid, session);
        logger.info("用户上线：{}", openid);

        // 连接MQTT
        MqttService mqttService = getMqttService();
        if (mqttService != null) {
            mqttService.connectUser(openid);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, org.springframework.web.socket.WebSocketMessage<?> message) throws Exception {
        String openid = (String) session.getAttributes().get("openid");
        String payload = message.getPayload().toString();
        logger.info("收到来自用户 {} 的消息：{}", openid, payload);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> messageMap = objectMapper.readValue(payload, HashMap.class);

            String deviceId = (String) messageMap.get("deviceId");
            String cmdType = (String) messageMap.get("cmdType");
            Map<String, Object> cmdContent = (Map<String, Object>) messageMap.get("cmdContent");

            if (deviceId == null || cmdType == null || cmdContent == null) {
                sendMessageToUser(openid, Map.of("status", "error", "message", "无效的命令格式（缺少deviceId/cmdType/cmdContent）"));
                return;
            }

            // 构建八大主题之一
            String topic = "";
            switch (cmdType) {
                case "control":
                    topic = "Switch/device/" + deviceId + "/properties/set/request";
                    break;
                case "query":
                    topic = "Switch/device/" + deviceId + "/properties/get/request";
                    break;
                case "event":
                    topic = "Switch/device/" + deviceId + "/events/report/request";
                    break;
                default:
                    sendMessageToUser(openid, Map.of("status", "error", "message", "无效的命令类型（支持control/query/event）"));
                    return;
            }

            // 发布到MQTT八大主题
            MqttService mqttService = getMqttService();
            if (mqttService != null) {
                mqttService.publish(openid, topic, messageMap);
                sendMessageToUser(openid, Map.of("status", "success", "message", "命令已发送至主题：" + topic));
            } else {
                sendMessageToUser(openid, Map.of("status", "error", "message", "MQTT服务未初始化"));
            }

        } catch (Exception e) {
            logger.error("处理WebSocket消息时出错：{}", e.getMessage(), e);
            sendMessageToUser(openid, Map.of("status", "error", "message", "处理命令时出错：" + e.getMessage()));
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String openid = (String) session.getAttributes().get("openid");
        sessionMap.remove(openid);
        logger.info("用户下线：{}", openid);

        // 断开MQTT连接
        MqttService mqttService = getMqttService();
        if (mqttService != null) {
            mqttService.disconnectUser(openid);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("WebSocket连接错误：{}", exception.getMessage(), exception);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void onApplicationEvent(MqttMessageEvent event) {
        String deviceId = event.getDeviceId();
        String openid = deviceUserService.getOpenidByDeviceId(deviceId);

        if (openid != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> payloadMap = objectMapper.readValue(event.getPayload(), HashMap.class);

                // 构建前端需要的JSON格式
                Map<String, Object> message = new HashMap<>();
                message.put("deviceId", deviceId);
                message.put("timestamp", System.currentTimeMillis());
                message.put("msgType", "deviceStatus ");
                message.put("content", payloadMap);
                message.put("ackRequired", false);

                sendMessageToUser(openid, message);
            } catch (Exception e) {
                logger.error("Failed to parse MQTT message: {}", e.getMessage(), e);
                // 发送错误消息到前端
                sendMessageToUser(openid, Map.of(
                        "topic", event.getTopic(),
                        "payload", "Failed to parse MQTT message",
                        "error", e.getMessage()
                ));
            }
        }
    }

    public void sendMessageToUser(String openid, Object message) {
        WebSocketSession session = sessionMap.get(openid);
        if (session != null && session.isOpen()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                logger.error("Failed to send message to user {}: {}", openid, e.getMessage(), e);
            }
        }
    }

    // 通过ApplicationContext获取MqttService，避免直接依赖
    private MqttService getMqttService() {
        // 实际项目中可以通过ApplicationContext获取，这里简化处理
        // 确保循环依赖已解决
        return ApplicationContextProvider.getBean(MqttService.class);
    }
}