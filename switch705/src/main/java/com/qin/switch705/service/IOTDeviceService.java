package com.qin.switch705.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qin.switch705.domain.IOTDevice;

import java.util.Map;


public interface IOTDeviceService extends IService<IOTDevice> {
// IService是MyBatis-Plus 提供的一个通用服务接口，它提供了基本的 CRUD（增删改查）操作。
//指定服务接口操作的实体类
IOTDevice getDeviceByDeviceId(String deviceId);
//根据设备ID查询该设备数据库的信息
boolean controlDeviceSwitch(String deviceId,int status);
//控制设备开关

}
