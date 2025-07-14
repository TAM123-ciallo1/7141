package com.itheima.springp.repository;

import com.itheima.springp.model.DeviceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceUserRepository extends JpaRepository<DeviceUser, Long> {
    Optional<DeviceUser> findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}