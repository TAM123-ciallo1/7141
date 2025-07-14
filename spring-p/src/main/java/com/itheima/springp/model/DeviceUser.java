package com.itheima.springp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "device_user")
public class DeviceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;
    private String openid;
}