package com.noah.starter.web.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

@Slf4j(topic = "OkHttpSingleLine")
public class OkHttpSingleLineLoggingInterceptor implements Interceptor {
    private final HttpLoggingInterceptor httpLoggingInterceptor;

    public OkHttpSingleLineLoggingInterceptor() {
        this.httpLoggingInterceptor = new HttpLoggingInterceptor(OkHttpSingleLineLogger.INSTANCE);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            OkHttpSingleLineLogger.start();
            return httpLoggingInterceptor.intercept(chain);
        } finally {
            String end = OkHttpSingleLineLogger.end();
            log.info(end);
        }
    }
}
