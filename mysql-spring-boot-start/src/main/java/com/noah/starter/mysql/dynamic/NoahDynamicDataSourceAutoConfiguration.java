package com.noah.starter.mysql.dynamic;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@AllArgsConstructor
@AutoConfigureBefore(value = DynamicDataSourceAutoConfiguration.class, name = "com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration")
public class NoahDynamicDataSourceAutoConfiguration {

    @Autowired
    private Environment env;

    @PostConstruct
    public void postConstruct() {
        log.info("NoahDynamicDataSourceAutoConfiguration dynamicEnabled:{}", env.getProperty("spring.datasource.dynamic.enabled"));
    }
}
