package com.itheima.springp.service;

import com.itheima.springp.model.DeviceUser;
import com.itheima.springp.repository.DeviceUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceUserService {

    @Autowired
    private DeviceUserRepository deviceUserRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String DEVICE_USER_KEY = "device:user";

    public String getOpenidByDeviceId(String deviceId) {
        return (String) redisTemplate.opsForHash().get(DEVICE_USER_KEY, deviceId);
    }

    @PostConstruct
    public void loadCache() {
        List<DeviceUser> list = deviceUserRepository.findAll();
        for (DeviceUser du : list) {
            redisTemplate.opsForHash().put(DEVICE_USER_KEY, du.getDeviceId(), du.getOpenid());
        }
        System.out.println("缓存加载完成，共 " + list.size() + " 条记录");
    }

    public void bindDevice(String deviceId, String openid) {
        DeviceUser du = new DeviceUser();
        du.setDeviceId(deviceId);
        du.setOpenid(openid);
        deviceUserRepository.save(du);
        redisTemplate.opsForHash().put(DEVICE_USER_KEY, deviceId, openid);
    }

    public void unbindDevice(String deviceId) {
        deviceUserRepository.deleteByDeviceId(deviceId);
        redisTemplate.opsForHash().delete(DEVICE_USER_KEY, deviceId);
    }
}