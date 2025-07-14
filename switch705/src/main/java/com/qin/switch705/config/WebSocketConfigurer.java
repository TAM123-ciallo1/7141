package com.qin.switch705.config;

import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

public interface WebSocketConfigurer {
void registerWebSocketHandlers(WebSocketHandlerRegistry registry);
}
