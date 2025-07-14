package com.itheima.springp.repository;

import com.itheima.springp.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {}