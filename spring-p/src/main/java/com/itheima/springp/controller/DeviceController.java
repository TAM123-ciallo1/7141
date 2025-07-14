package com.itheima.springp.controller;

import com.itheima.springp.service.DeviceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceUserService deviceUserService;

    @PostMapping("/bind")
    public String bind(@RequestParam String deviceId, @RequestParam String openid) {
        deviceUserService.bindDevice(deviceId, openid);
        return "绑定成功";
    }

    @PostMapping("/unbind")
    public String unbind(@RequestParam String deviceId) {
        deviceUserService.unbindDevice(deviceId);
        return "解绑成功";
    }
}