package com.qin.switch705.controller;

import com.qin.switch705.API.CommonResult;
import com.qin.switch705.domain.IOTDevice;
import com.qin.switch705.service.IOTDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController//所有方法默认返回 JSON 格式数据
@RequestMapping("/api/device")//设置该控制器所有接口的基础路径
public class IOTDeviceController {
@Autowired
    private IOTDeviceService iotDeviceService;
@GetMapping("/{deviceId}")
    public CommonResult<IOTDevice> getDevice(@PathVariable String deviceId){
    IOTDevice device=iotDeviceService.getDeviceByDeviceId(deviceId);
    if(device!=null){
        return CommonResult.success(device);
    }else{
        return CommonResult.fail("没有发现设备");
    }
}
@PostMapping("/{deviceId}/control")
    public CommonResult<Boolean> controlDevice(@PathVariable String deviceId,@RequestParam int status){
    boolean result=iotDeviceService.controlDeviceSwitch(deviceId,status);
    if(result){
        return CommonResult.success(true,"control is success");
    }else{
        return CommonResult.fail("控制设备失败");
    }
}
}
