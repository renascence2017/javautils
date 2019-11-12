package com.api.reflection.service;

import com.alibaba.fastjson.JSONObject;


public interface ProviderSendInterface {

    // 返回RouteResponseDTO结构中的rspTime，是调用供应商的耗时。请在调用前后用System.currentTimeMillis()计算差值
    RouteResponseDTO sendQuery(JSONObject json, String queryPath, String requestMethod);
}
