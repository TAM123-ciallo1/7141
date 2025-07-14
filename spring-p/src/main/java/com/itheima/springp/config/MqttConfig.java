package com.itheima.springp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.client-id-prefix:yuanqin}")
    private String clientIdPrefix;

    @Bean
    public MqttClientFactory mqttClientFactory() {
        return new MqttClientFactory(broker, username, password, clientIdPrefix);
    }
}