package com.qin.switch705.service;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@MessagingGateway(defaultReplyChannel = "mqttOutboundChannelIOT")
public interface MqttGateWayService {
    void sendMessageToMqtt(String data);
    void sendMessageToMqtt(String data, @Header(MqttHeaders.TOPIC)String topic);
    void sendMessageToMqtt(String data,@Header(MqttHeaders.TOPIC) String topic,@Header(MqttHeaders.QOS) int qos);
}
