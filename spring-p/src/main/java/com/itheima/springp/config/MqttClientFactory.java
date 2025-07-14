package com.itheima.springp.config;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.UUID;

public class MqttClientFactory {
    private final String broker;
    private final String username;
    private final String password;
    private final String clientIdPrefix;

    public MqttClientFactory(String broker, String username, String password, String clientIdPrefix) {
        this.broker = broker;
        this.username = username;
        this.password = password;
        this.clientIdPrefix = clientIdPrefix;
    }

    /**
     * 创建唯一的MQTT客户端（适用于多用户场景）
     * @param userId 用户ID（用于生成唯一客户端ID）
     * @return 配置好的MQTT客户端
     */
    public MqttClient createClient(String userId) throws MqttException {
        // 生成唯一客户端ID：前缀-用户ID-随机UUID
        String clientId = String.format("%s-%s-%s",
                clientIdPrefix,
                userId,
                UUID.randomUUID().toString().substring(0, 8));
//                                                               用内存存消息
        MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true); // 根据需要设置
        options.setUserName(username);
        options.setPassword(password.toCharArray());

        // 设置连接超时和心跳
        options.setConnectionTimeout(30); // 秒
        options.setKeepAliveInterval(60); // 秒

        // 设置回调（可选）
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                System.out.println("MQTT连接成功: " + (reconnect ? "重连" : "首次连接"));
            }

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("MQTT连接丢失: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                System.out.println("收到消息: " + new String(message.getPayload()) + " from topic: " + topic);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("消息发送完成");
            }
        });

        client.connect(options);
        return client;
    }
}