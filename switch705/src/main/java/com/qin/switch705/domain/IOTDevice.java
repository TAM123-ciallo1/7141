package com.qin.switch705.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@TableName(value="tb_device_info")
public class IOTDevice {
//Serializable  是一个来自 Java 自带的java.io包的标记接口，
// 用于指定一个对象可以被序列化。序列化是将对象转换为字节流的过程，以便存储到文件中或通过网络传输。
    @TableId(type= IdType.AUTO)
    private Integer id;

     @TableField(value="device_id")
     private String deviceId;

    @TableField(value="user")
     private String user;

    @TableField(value="status")
    private int status;


    @TableField(value="error1")
    private int error1;

    @TableField(value="error2")
    private int error2;

    @TableField(value="error3")
    private int error3;
    public Integer getId() {
        return id;
    }
    public Map<String, Object> getMap() {
        return new HashMap<>() {{
            put("device_id", getDeviceId());
            put("switchStatus", getStatus());
            // 其他字段...
        }};
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
       return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getError1() {
        return error1;
    }

    public void setError1(int error1) {
        this.error1 = error1;
    }

    public int getError2() {
        return error2;
    }

    public void setError2(int error2) {
        this.error2 = error2;
    }

    public int getError3() {
        return error3;
    }

    public void setError3(int error3) {
        this.error3 = error3;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    //显式定义serialVersionUID可以避免因类版本变化而导致的反序列化问题

}
