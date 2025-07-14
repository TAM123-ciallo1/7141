package com.itheima.springp.service;

import com.itheima.springp.model.User;
import com.itheima.springp.repository.UserRepository;
import com.itheima.springp.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public String login(String openid) {
        // 简单模拟：如果用户不存在就创建
        if (!userRepository.existsById(openid)) {
            User user = new User();
            user.setOpenid(openid);
            user.setNickname("用户" + openid);
            userRepository.save(user);
        }
        return jwtUtils.generateToken(openid);
    }
}