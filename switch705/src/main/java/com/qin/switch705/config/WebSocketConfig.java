package com.qin.switch705.config;
//注册WebSocket处理器和路径
import com.qin.switch705.service.WebSocketService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    private final WebSocketService webSocketService;
    public WebSocketConfig(WebSocketService webSocketService){
        this.webSocketService=webSocketService;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){registry.addHandler(webSocketService,"/ws/device/{deviceId}").setAllowedOrigins("*");}

}
