package com.itheima.springp.event;

import org.springframework.context.ApplicationEvent;

public class MqttMessageEvent extends ApplicationEvent {
    private String topic;
    private String payload;
    private String deviceId;

    public MqttMessageEvent(Object source, String topic, String payload, String deviceId) {
        super(source);
        this.topic = topic;
        this.payload = payload;
        this.deviceId = deviceId;
    }

    public String getTopic() {
        return topic;
    }

    public String getPayload() {
        return payload;
    }

    public String getDeviceId() {
        return deviceId;
    }
}