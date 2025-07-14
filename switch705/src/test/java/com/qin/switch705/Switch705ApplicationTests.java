package com.qin.switch705;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

// 非public类，避免与文件名强制关联（若类名与文件名一致，可保留public）
class MqttConnectionTest {

    @Value("${spring.mqtt.url}")
    private String brokerUrl;

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Test
    void testMqttConnection() throws MqttException {
        // 生成唯一客户端 ID
        String clientId = "test-client-" + UUID.randomUUID();

        // 使用 try-with-resources 自动关闭 MqttClient，避免资源泄漏
        try (MqttClient client = new MqttClient(brokerUrl, clientId)) {
            // 设置连接选项
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(30); // 连接超时时间（秒）
            options.setKeepAliveInterval(60); // 心跳间隔（秒）

            // 连接 MQTT 服务器
            client.connect(options);
            System.out.println("✅ MQTT 连接成功: " + brokerUrl);

            // 验证连接状态
            if (client.isConnected()) {
                System.out.println("✅ 客户端已连接，客户端 ID: " + clientId);
            } else {
                System.err.println("❌ 连接失败，客户端未处于连接状态");
            }
        } catch (MqttException e) {
            System.err.println("❌ MQTT 连接异常: " + e.getMessage());
            e.printStackTrace(); // 打印详细异常栈，方便排查问题
        }
    }
}