package com.itheima.springp.config;

import com.itheima.springp.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

// 标记为Spring组件，允许注入JwtUtils
@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    // 注入你的JwtUtils，复用已有的Token解析逻辑
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
        // 步骤1：从请求路径中提取{openid}（如路径为/websocket/user333，则提取user333）
        String path = request.getURI().getPath(); // 示例：/websocket/user333
        String[] pathSegments = path.split("/");
        if (pathSegments.length < 3) {
            // 路径格式错误（未包含openid）
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }
        String pathOpenid = pathSegments[2]; // 提取路径中的openid


        // 步骤2：从请求头中提取Token并验证
        String token = request.getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            // 无Token或格式错误
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        token = token.substring(7); // 移除"Bearer "前缀


        // 步骤3：使用JwtUtils解析Token，验证有效性并获取用户openid
        if (!jwtUtils.validateToken(token)) {
            // Token无效（过期、篡改等）
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        String tokenOpenid = jwtUtils.getOpenidFromToken(token); // 从Token中解析出的openid


        // 步骤4：验证路径中的openid与Token中的openid是否一致
        if (!pathOpenid.equals(tokenOpenid)) {
            // 不一致，拒绝连接
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }


        // 验证通过，将openid存入会话属性供后续使用
        attributes.put("openid", tokenOpenid);
        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        // 握手后无需处理
    }
}