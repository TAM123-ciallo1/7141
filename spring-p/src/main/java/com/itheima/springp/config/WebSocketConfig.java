package com.itheima.springp.config;

import com.itheima.springp.security.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.itheima.springp.service.WebSocketServer;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtUtils jwtUtils;

    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    public WebSocketConfig(JwtUtils jwtUtils, WebSocketAuthInterceptor webSocketAuthInterceptor) {
        this.jwtUtils = jwtUtils;
        this.webSocketAuthInterceptor = webSocketAuthInterceptor;
    }
    @Bean
    public WebSocketServer webSocketServer() {
        return new WebSocketServer();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketServer(), "/websocket/{openid}")
                .addInterceptors(webSocketAuthInterceptor)
                .setAllowedOrigins("*")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        if (request instanceof org.springframework.http.server.ServletServerHttpRequest) {
                            org.springframework.http.server.ServletServerHttpRequest servletRequest =
                                    (org.springframework.http.server.ServletServerHttpRequest) request;
                            String token = servletRequest.getServletRequest().getHeader("Authorization");
                            if (token != null && token.startsWith("Bearer ")) {
                                token = token.substring(7);
                                String openid = jwtUtils.getOpenidFromToken(token);
                                if (openid != null) {
                                    attributes.put("openid", openid);
                                    return true;
                                }
                            }
                        }
                        return false;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                        // 无需处理
                    }
                });
    }
}