package com.noah.starter.web.okhttp;

import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpSingleLineLogger implements HttpLoggingInterceptor.Logger {
    private static ThreadLocal<StringBuilder> tl = new ThreadLocal<>();
    public static OkHttpSingleLineLogger INSTANCE = new OkHttpSingleLineLogger();

    private OkHttpSingleLineLogger() {
    }

    public static void start() {
        tl.set(new StringBuilder());
    }

    public static String end() {
        StringBuilder stringBuilder = tl.get();
        if (stringBuilder != null) {
            tl.remove();
            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    @Override
    public void log(String message) {
        StringBuilder sb = tl.get();
        sb.append("\n -- ").append(message);
    }
}
