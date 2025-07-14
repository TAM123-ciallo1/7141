package com.qin.switch705.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qin.switch705.domain.IOTDevice;
import com.qin.switch705.domain.user;
import com.qin.switch705.mapper.IOTDeviceMapper;
import com.qin.switch705.mapper.UserMapper;
import com.qin.switch705.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, user> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IOTDeviceMapper deviceMapper;
@Override
    //确保每个方法中的数据库操作要么全部成功，要么全部失败，从而维护数据的一致性和完整性
    public user login(String username,String password){
    user user=userMapper.selectByUsername(username);
    if(user==null){
        throw new RuntimeException("no found user");
    }
    if(!user.getPassword().equals(password)){
        throw new RuntimeException("not right password");
    }
    return user;
}
    @Override
    @Transactional
    public user createSubAccount(String parentUsername, String subUsername, String password) {
        user parentUser = userMapper.selectByUsername(parentUsername);
        if (parentUser == null) {
            throw new RuntimeException("父账户不存在");
        }
        user existingUser = userMapper.selectByUsername(subUsername);
        if (existingUser != null) {
            throw new RuntimeException("子账户名称已存在");
        }
        user subUser = new user();
        subUser.setUsername(subUsername);
        subUser.setPassword(password);
        subUser.setControl(parentUser.getControl());
        userMapper.insert(subUser);
        return subUser;
    }
    @Override
    public IOTDevice getControlByUserName(String username) {
        user user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        String deviceId = user.getControl();
        if (deviceId == null || deviceId.isEmpty()) {
            throw new RuntimeException("用户未关联设备");
        }

        return deviceMapper.selectByDeviceId(deviceId);
    }
}
