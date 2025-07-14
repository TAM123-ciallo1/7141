package com.qin.switch705.service;

import com.alibaba.fastjson.JSON;
import com.qin.switch705.domain.IOTDevice;
import com.qin.switch705.mapper.IOTDeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


@Service//继承文本消息处理器
public class WebSocketService extends TextWebSocketHandler {
    private final ConcurrentHashMap<String,CopyOnWriteArraySet<WebSocketSession>> deviceSessionMap=new ConcurrentHashMap<>();
    //初始化并命名deviceSessionMap线程安全映射表
    //键为设备id,值为存储WebSocketSession对象的线程安全集合（CopyOnWriteArraySet）->保证遍历与修改操作不会·产生并发冲突允许多用户同时操控同一台设备
    //ConcurrentHashMap允许多个线程同时读写
    //CopyOnWriteArraySet<WebSocketSession>线程安全的集合，存储所有订阅该设备的客户端会话
    @Autowired
    private IOTDeviceMapper deviceMapper;
private IOTDevice getDeviceByDeviceId(String deviceId){
    return deviceMapper.selectByDeviceId(deviceId);
}
private String extractDeviceId(WebSocketSession session){
    String path=session.getUri().getPath();
    String[] parts=path.split("/");
    if(parts.length>=4&&"device".equals(parts[2])){
        return parts[3];//-----ws://服务器地址/ws/device/{Switch01012}
    }
    return null;
}
@Override//服务器连接后自动调用
    public void afterConnectionEstablished(WebSocketSession session)throws IOException{
    String deviceId=extractDeviceId(session);
    if(deviceId==null){
        session.close();//提取设备id为空直接关
        return;}
    CopyOnWriteArraySet<WebSocketSession> sessions = deviceSessionMap.computeIfAbsent(deviceId, k -> new CopyOnWriteArraySet<>());
    sessions.add(session);
    //把当前会话添加到对应设备id会话集合中
       IOTDevice device=getDeviceByDeviceId(deviceId);
       if(device!=null){
           String json= JSON.toJSONString(device);
           session.sendMessage(new TextMessage(json));
       }
}
}
