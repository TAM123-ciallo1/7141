package com.qin.switch705.API;

public enum ResultCode implements IErrorCode{
    //集中定义系统所有错误码，实现 IErrorCode 接口
    SUCCESS(200,"操作成功"),
    FAILURE(400,"操作失败");

    private final long code;
    private final String message;
    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public long getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
