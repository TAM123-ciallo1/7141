package com.qin.switch705.constant;


public class MqttTopicConstants {

    public static final String PROPERTY_REPORT_REQUEST = "Switch/device/%s/properties/report/request";

    public static final String PROPERTY_REPORT_RESPONSE = "Switch/device/%s/properties/report/response";


    public static final String PROPERTY_SET_REQUEST = "Switch/device/%s/properties/set/request";

    public static final String PROPERTY_SET_RESPONSE = "Switch/device/%s/properties/set/response";


    public static final String PROPERTY_GET_REQUEST = "Switch/device/%s/properties/get/request";

    public static final String PROPERTY_GET_RESPONSE = "Switch/device/%s/properties/get/response";


    public static final String EVENT_REPORT_REQUEST = "Switch/device/%s/events/report/request";

    public static final String EVENT_REPORT_RESPONSE = "Switch/device/%s/events/report/response";


    public static String getTopic(String template, String deviceId) {
        return String.format(template, deviceId);
    }
}