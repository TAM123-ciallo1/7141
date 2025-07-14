package com.qin.switch705.config;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

@Data
@Configuration
public class MqttConfiguration {
    @Value("${spring.mqtt.defaultQos:1}")
    private int defaultQos;
    @Value("${spring.mqtt.username}")
    private String username;
    @Value("${spring.mqtt.password}")
    private String password;
    @Value("${spring.mqtt.url}")
    private String brokerUrl;
    @Value("${spring.mqtt.pubClientId}")
    private String pubClientId;
    @Value("${spring.mqtt.subClientId}")
    private String subClientId;
    @Value("${spring.mqtt.subTopic}")
    private String subTopic;
    // 已有的包和注解等无需改动，直接在类里添加下面这些 getter、setter 方法
    public int getDefaultQos() {
        return defaultQos;
    }

    public void setDefaultQos(int defaultQos) {
        this.defaultQos = defaultQos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getPubClientId() {
        return pubClientId;
    }

    public void setPubClientId(String pubClientId) {
        this.pubClientId = pubClientId;
    }

    public String getSubClientId() {
        return subClientId;
    }

    public void setSubClientId(String subClientId) {
        this.subClientId = subClientId;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }
    public String[] getUrl() {
        return new String[]{brokerUrl};
    }

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory=new DefaultMqttPahoClientFactory();
        String[] mqttServerUrls=getUrl();
        MqttConnectOptions options=new MqttConnectOptions();
        options.setServerURIs(mqttServerUrls);

        options.setUserName(getUsername());
        options.setPassword(getPassword().toCharArray());
        options.setKeepAliveInterval(2);

        options.setCleanSession(false);
        factory.setConnectionOptions(options);
        return factory;
    }
}
