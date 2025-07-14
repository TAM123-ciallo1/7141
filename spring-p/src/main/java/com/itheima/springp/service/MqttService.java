package com.itheima.springp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.springp.event.MqttMessageEvent;
import com.itheima.springp.config.MqttClientFactory;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class MqttService {

    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    @Autowired
    private MqttClientFactory clientFactory;

    @Autowired
    private DeviceUserService deviceUserService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private final ConcurrentHashMap<String, MqttClient> userClients = new ConcurrentHashMap<>();

    private static final String[] TOPICS = {
            "Switch/device/+/properties/report/request",
            "Switch/device/+/properties/report/response",
            "Switch/device/+/properties/set/request",
            "Switch/device/+/properties/set/response",
            "Switch/device/+/properties/get/request",
            "Switch/device/+/properties/get/response",
            "Switch/device/+/events/report/request",
            "Switch/device/+/events/report/response"
    };

    public void connectUser(String userId) {
        try {
            disconnectUser(userId);
            MqttClient client = clientFactory.createClient(userId);
            userClients.put(userId, client);

            for (String topic : TOPICS) {
                client.subscribe(topic, (receivedTopic, message) ->
                        handleMessage(receivedTopic, message, userId)
                );
            }
        } catch (MqttException e) {
            logger.error("Failed to connect user {} to MQTT: {}", userId, e.getMessage(), e);
        }
    }

    private void handleMessage(String topic, MqttMessage message, String userId) {
        String deviceId = topic.split("/")[2];

        // 发布事件而不是直接调用WebSocket
        eventPublisher.publishEvent(
                new MqttMessageEvent(
                        this,
                        topic,
                        new String(message.getPayload()),
                        deviceId
                )
        );
    }

    public void publish(String userId, String topic, Object payload) {
        MqttClient client = userClients.get(userId);
        if (client == null || !client.isConnected()) {
            logger.error("User {} is not connected to MQTT", userId);
            throw new IllegalStateException("用户" + userId + "未连接MQTT");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(payload);
            MqttMessage message = new MqttMessage(jsonPayload.getBytes());
            message.setQos(1);
            client.publish(topic, message);
            logger.info("已发布消息到MQTT - 主题: {}, 内容: {}", topic, jsonPayload);
        } catch (Exception e) {
            logger.error("Failed to publish message to MQTT for user {}: {}", userId, e.getMessage(), e);
        }
    }

    public void disconnectUser(String userId) {
        MqttClient client = userClients.remove(userId);
        if (client != null && client.isConnected()) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                logger.error("Failed to disconnect user {} from MQTT: {}", userId, e.getMessage(), e);
            }
        }
    }
}