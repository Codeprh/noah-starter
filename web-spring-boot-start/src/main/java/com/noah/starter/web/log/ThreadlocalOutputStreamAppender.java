package com.noah.starter.web.log;

import ch.qos.logback.core.OutputStreamAppender;

public class ThreadlocalOutputStreamAppender<T> extends OutputStreamAppender<T> {
    public ThreadlocalOutputStreamAppender() {
        setOutputStream(ThreadlocalOutputStream.INSTANCE);
    }
}
