package com.noah.starter.web.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @description:
 * @author: hang
 * @create: 2022/9/5
 **/
public class ThreadlocalOutputStream extends OutputStream {
    private static final int MB = 1024 * 1024;

    public static ThreadlocalOutputStream INSTANCE = new ThreadlocalOutputStream();

    private ThreadlocalOutputStream() {
    }

    private static ThreadLocal<LengthByteArrayOutputStream> tl = new ThreadLocal<>();

    public static void start(){
        tl.set(new LengthByteArrayOutputStream());
    }

    public static String stop(){
        ByteArrayOutputStream os = tl.get();
        tl.remove();
        if (os != null){
            return os.toString();
        }
        return null;
    }

    @Override
    /**
     * 这个方法里千万别log了。。会死循环的。。
     */
    public void write(int b) throws IOException {
        LengthByteArrayOutputStream os = tl.get();
        if (os == null){
            return;
        }
        if (os.getCount() > 4 * MB){
            System.out.println("灾难.. LengthByteArrayOutputStream长度超过4M了.. thread = {}" + Thread.currentThread().getName());
            return;
        }
        if (os.getCount() > MB){
            System.out.println("警告.. LengthByteArrayOutputStream长度超过1M了.. thread = {}" + Thread.currentThread().getName());
        }
        os.write(b);

    }

    static class LengthByteArrayOutputStream extends ByteArrayOutputStream{
        public int getCount(){
            return count;
        }
    }
}
