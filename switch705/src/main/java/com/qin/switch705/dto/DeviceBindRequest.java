package com.qin.switch705.dto;
//封装请求
import lombok.Data;

@Data
public class DeviceBindRequest {
    private String deviceId;
    private String key;

}
