package com.qin.switch705.API;

public interface IErrorCode {
    //定义错误码的标准接口，所有错误码必须实现这两个方法
    long getCode();
    String getMessage();
}
