package com.qin.switch705.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qin.switch705.domain.IOTDevice;
import com.qin.switch705.mapper.IOTDeviceMapper;
import com.qin.switch705.service.IOTDeviceService;
import com.qin.switch705.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
//生成一个名为log的日志记录器实例
@Service
public class IOTDeviceServiceImpl extends ServiceImpl<IOTDeviceMapper, IOTDevice> implements IOTDeviceService {
    @Autowired
    private MessageChannel mqttOutboundChannel;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IOTDeviceMapper iotDeviceMapper;
@Override
    public IOTDevice getDeviceByDeviceId(String deviceId){
         return iotDeviceMapper.selectOne(new QueryWrapper<IOTDevice>().eq("device_id", deviceId));
     }
    @Override
    public boolean controlDeviceSwitch(String deviceId, int status) {
        // 1. 校验状态值
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error1", false); // 缺相
        errorDetails.put("error2", false); // 过载
        errorDetails.put("error3", false); // 漏电

        Map<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("hasError", 0);     // 无异常
        errorInfo.put("details", errorDetails);
        errorInfo.put("errorMessage", ""); // 空描述

        // 完整消息体
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("deviceId", deviceId);    // 设备唯一标识
        payloadMap.put("status", 0);            // 控制命令中不关心当前状态，填0
        payloadMap.put("Controller", status);    // 核心控制命令
        payloadMap.put("errorInfo", errorInfo);  // 必须包含，保持格式统一

        String payload = JSON.toJSONString(payloadMap);

        // 3. 发送MQTT消息
        String topic = "Switch/device/" + deviceId + "/properties/set/request";
        Message<?> message = MessageBuilder.withPayload(payload)
                .setHeader(MqttHeaders.TOPIC, topic)
                .setHeader(MqttHeaders.QOS, 1)
                .build();

        return mqttOutboundChannel.send(message);
    }

}
