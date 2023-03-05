package com.noah.starter.demo.web.xxl;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;

import java.io.ByteArrayInputStream;
import java.io.File;

class TypeUseXxlTest {

    public static void main(String[] args) {
        RandomUtil.randomInt(100);
        File file = null;
        String fileUrl = "https://static.thenounproject.com/png/3296108-200.png";
        byte[] bytes = HttpUtil.downloadBytes(fileUrl);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        String name = FileUtil.getName(fileUrl);
        file = FileUtil.file(name);
        FileUtil.writeFromStream(byteArrayInputStream, file);
        System.out.println("hello");
    }

}