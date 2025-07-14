package com.qin.switch705.config;

import com.qin.switch705.domain.IOTDevice;
import com.qin.switch705.service.IOTDeviceService;
import com.qin.switch705.utils.JSONUtil;
import com.qin.switch705.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.*;
//配置接收mqtt消息
import java.util.Arrays;
import java.util.UUID;

@Configuration//布局配置
@IntegrationComponentScan//集成组件扫描
public class MqttInboundConfiguration {
    private static Logger LOGGER =LoggerFactory.getLogger(MqttInboundConfiguration.class);
    //创建日志记录器
    @Autowired
    private IOTDeviceService deviceService;
    @Autowired
    private MqttConfiguration mqttConfiguration;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MqttPahoClientFactory mqttClientFactory;
    //注入MQTT客户端工厂，用于创建 MQTT 客户端。
    @Bean
    public MessageChannel mqttInputChannel(){
        return new DirectChannel();
    }
    //接收MQTT消息，直接通道，消息会直接传递给订阅了该通道的消费者。
@Bean
    public MessageProducer mqttInboundAdapter(){
    String[] topics = mqttConfiguration.getSubTopic().split(",");
    //下面适配器用于将 MQTT 消息转换为 Spring Integration 消息
    MqttPahoMessageDrivenChannelAdapter adapter =
            new MqttPahoMessageDrivenChannelAdapter(
                    mqttConfiguration.getSubClientId(),
                    mqttClientFactory,
                    topics
            );
    int[] qosLevels = new int[topics.length];
    Arrays.fill(qosLevels, 1); // 为每个主题设置QoS为1
    adapter.setQos(qosLevels);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setOutputChannel(mqttInputChannel());
    return adapter;
    }
@Bean
    @ServiceActivator(inputChannel="mqttInputChannel")
    public MessageHandler handler(){
        return new MessageHandler(){
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                Object payload=message.getPayload();
                MessageHeaders messageHeaders=message.getHeaders();
                UUID packetId=messageHeaders.getId();
                Object qos=messageHeaders.get(MqttHeaders.QOS);
                String recvTopic = (String) messageHeaders.get(MqttHeaders.RECEIVED_TOPIC);  // 获取消息接收的主题
                assert  recvTopic!=null;//断言（assert）语句，用于确保  recvTopic  不为  null
                LOGGER.info("收到MQTT消息: topic={}, payload={}", recvTopic, payload);
                if (recvTopic.startsWith("Switch/device")){
                    IOTDevice device= JSONUtil.toBean((String)payload,IOTDevice.class);
                    if(device!=null){
                        deviceService.save(device);
                        redisUtils.lLeftPush("deviceId"+device.getDeviceId()+"info:",JSONUtil.toJsonString(device));
                    }
                }
            }
    };
}

}
