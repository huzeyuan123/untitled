package com.yonyou.config;

import lombok.Data;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;



@Data
public class TestConfig {
    //带参数get请求url
    public static String getWithParamUrl;
    //登录请求url
    public static String loginUrl;

    //cookies
    public static CookieStore store;

    //声明http客户端
    public static DefaultHttpClient defaultHttpClient;
}
