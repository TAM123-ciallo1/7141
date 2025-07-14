package com.itheima.springp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Device {
    @Id
    private String deviceId;
    private String name;
    private String status; // 运行状态
    private String exception; // 异常信息，可定义为无/三种异常的字符串表示
}