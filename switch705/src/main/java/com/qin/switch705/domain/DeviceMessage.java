package com.qin.switch705.domain;

import lombok.Data;

import java.util.Objects;
@Data
public class DeviceMessage {
    private String deviceId;
    private Integer status;
    private Integer controller;
    private ErrorInfo errorInfo;
    private static class ErrorInfo{
        private Integer hasError;
        private ErrorDetails details;
        private String errorMessage;
    }
    public static class ErrorDetails{
        private Integer error1;
        private Integer error2;
        private Integer error3;
    }

    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    // 自动生成的 toString() 示例
    @Override
    public String toString() {
        return "DeviceMessage{" +
                "deviceId='" + deviceId + '\'' +
                ", status=" + status +
                ", controller=" + controller +
                ", errorInfo=" + errorInfo +
                '}';
    }

    // 自动生成的 equals() 和 hashCode() 示例（基于所有字段）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceMessage that = (DeviceMessage) o;
        return Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(controller, that.controller) &&
                Objects.equals(errorInfo, that.errorInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, status, controller, errorInfo);
    }
}
