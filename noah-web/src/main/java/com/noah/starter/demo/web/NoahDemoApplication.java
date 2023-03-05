package com.noah.starter.demo.web;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.dianping.cat.servlet.CatFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableApolloConfig({"application", "switch"})
@MapperScan("com.noah.starter.demo.web.mybatis.mapper")
public class NoahDemoApplication {

    public static void main(final String[] args) {
        SpringApplication.run(NoahDemoApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean catFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CatFilter filter = new CatFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("cat-filter");
        registration.setOrder(1);
        return registration;
    }

}
