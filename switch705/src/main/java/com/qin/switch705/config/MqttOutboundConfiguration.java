package com.qin.switch705.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttOutboundConfiguration {
@Autowired
    private MqttConfiguration mqttConfiguration;
@Bean
    public MessageChannel mqttOutboundChannel(){return new DirectChannel();}
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutboundHandler(){
    MqttPahoMessageHandler handler=new MqttPahoMessageHandler(
            mqttConfiguration.getPubClientId(),
            mqttConfiguration.mqttPahoClientFactory()
    );
    handler.setAsync(true);
    handler.setDefaultQos(mqttConfiguration.getDefaultQos());
    handler.setDefaultRetained(false);
    handler.setConverter(new DefaultPahoMessageConverter());
return handler;
}
}
