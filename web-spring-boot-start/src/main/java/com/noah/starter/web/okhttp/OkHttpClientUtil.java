package com.noah.starter.web.okhttp;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: hang
 * @create: 2022/6/13
 **/
public class OkHttpClientUtil {
    private static OkHttpClient okHttpClient;

    static  {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(10, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(10, TimeUnit.SECONDS)
                .followRedirects(false)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .pingInterval(10, TimeUnit.SECONDS)
                .addInterceptor(new OkHttpSingleLineLoggingInterceptor())
                .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
                .build();
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}
