package com.qin.switch705.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONUtil {
    public static<T> T toBean(String json,Class<T> clazz){
return JSON.parseObject(json,clazz);
    }//把JSON字符串转化为java对象
    public static String toJsonString(Object obj){
        return JSON.toJSONString(obj);
    }//把对象转化为JSON字符串
    public static JSONObject parseObj(String json){
        return JSON.parseObject(json);
    }
}
