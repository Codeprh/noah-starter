package com.noah.starter.demo.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.noah.starter.demo.web.mybatis.entity.Product;
import com.noah.starter.demo.web.mybatis.service.IProductService;
import com.noah.starter.mysql.offset.NoahPage;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class HelloController {

    @ApolloJsonValue("${test.value:0}")
    public static Integer testValue;

    @ApolloJsonValue("${test.json}")
    public static TestJson testJson;

    @ApolloJsonValue("${test.str:aaa}")
    public static String testStr;

    @ApolloJsonValue("${test.no.value:99}")
    public static Integer testNoValue;

    @Resource
    private IProductService iProductService;

    @Data
    static class TestJson {
        private Integer aa;
    }

    @GetMapping("hi")
    public void hi() {
        log.info("hi");
        log.error("hi i am error");
        log.info("db query=" + iProductService.getById(88L).toString());
    }

    @SneakyThrows
    @GetMapping("/hello")
    public void hello() {

        while (true) {

            log.info("testValue:{}", testValue);
            log.info("testNoValue:{}", testNoValue);
            log.info("testStr:{}", testStr);
            log.info("testJson:{}", testJson.toString());

            log.info("我是info级别日志");
            log.error("我是error级别日志");
            log.warn("我是warn级别日志");
            log.debug("我是debug级别日志");

            log.info("db query=" + iProductService.getById(88L).toString());

            //page();
            //updateAll();
            TimeUnit.SECONDS.sleep(5);
        }

    }

    public void updateAll() {
        Product p = new Product();

        p.setId(1000L);
        p.setName("1000-name");
        p.setStock(100);

        // com.baomidou.mybatisplus.core.exceptions.MybatisPlusException: Prohibition of table update operation
        iProductService.saveOrUpdate(p, null);
    }

    public void page() throws InterruptedException {

        long offset = 0;
        long count = 2;

        NoahPage<Product> productNoahPage = new NoahPage<>(offset, count);
        productNoahPage.setSearchCount(true);

        while (Objects.nonNull(productNoahPage = iProductService.page(productNoahPage)) && CollectionUtil.isNotEmpty(productNoahPage.getRecords())) {
            List<Product> records = productNoahPage.getRecords();
            log.info("page meta offset:{},count:{},productNoahPage:{}", productNoahPage.offset(), productNoahPage.getSize(), JSONUtil.toJsonStr(productNoahPage));

            offset = productNoahPage.offset() + productNoahPage.getSize();
            count = productNoahPage.getSize();

            productNoahPage = new NoahPage<Product>(offset, count);
            productNoahPage.setSearchCount(true);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
