package com.qin.switch705.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qin.switch705.domain.IOTDevice;
import com.qin.switch705.domain.user;

import java.util.List;

public interface UserService extends IService<user> {
    user login(String username,String password);//登录
    user createSubAccount(String parentUsername,String subUsername,String password);//添加子用户
    IOTDevice getControlByUserName(String username);//控制设备开关
//根据用户账号查询可控制设备
}
