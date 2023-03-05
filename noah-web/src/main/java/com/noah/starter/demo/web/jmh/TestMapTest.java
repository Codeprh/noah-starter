package com.noah.starter.demo.web.jmh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestMapTest {
    public static void main(String[] args) {

        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("1", "1");

        System.out.println(map.computeIfAbsent("2", k -> "2"));
    }
}
