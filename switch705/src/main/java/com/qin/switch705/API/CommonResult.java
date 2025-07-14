package com.qin.switch705.API;

import lombok.Data;

@Data
public class CommonResult<T> {
    // 用于封装 API 请求的响应结果
    private long code;
    private String message;
    private T data;

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // （带数据和默认消息）
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    // （带数据和自定义消息）
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    // （无数据，仅自定义消息）
    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), message, null);
    }

    // 使用预定义错误码）
    public static <T> CommonResult<T> fail(ResultCode errorCode) {
        return new CommonResult<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    // （使用预定义错误码并自定义消息）
    public static <T> CommonResult<T> fail(ResultCode errorCode, String message) {
        return new CommonResult<>(errorCode.getCode(), message, null);
    }

    // 失败响应（自定义错误码和消息）
    public static <T> CommonResult<T> fail(long code, String message) {
        return new CommonResult<>(code, message, null);
    }

    // 失败响应（仅自定义消息，使用默认错误码 400）
    public static <T> CommonResult<T> fail(String message) {
        return new CommonResult<>(ResultCode.FAILURE.getCode(), message, null);
    }

    // 链式调用：设置自定义消息
    public CommonResult<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    // 链式调用：设置自定义错误码
    public CommonResult<T> withCode(long code) {
        this.code = code;
        return this;
    }
}