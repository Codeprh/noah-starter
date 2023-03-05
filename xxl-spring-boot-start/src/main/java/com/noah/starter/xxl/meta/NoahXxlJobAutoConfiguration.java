package com.noah.starter.xxl.meta;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "noah.xxl-job.register", name = "enable", havingValue = "true", matchIfMissing = true)
public class NoahXxlJobAutoConfiguration {

    /**
     * http请求-bean
     *
     * @return
     */
    @Bean
    public OkHttpClient xxlJobOkHttpClient() {

        //日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(10, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(10, TimeUnit.SECONDS)
                .followRedirects(false)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .pingInterval(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    /**
     * xxl-job请求客户端
     *
     * @param username
     * @param password
     * @param address
     * @param xxlJobOkHttpClient
     * @return
     */
    @Bean
    public XxlJobAdminWebClient xxlJobAdminWebClient(
            @Value("${xxl-job.web.username:admin}") String username,
            @Value("${xxl-job.web.password:123456}") String password,
            @Value("${xxl-job.web.address:http://127.0.0.1:8080/xxl-job-admin}") String address,
            @Autowired OkHttpClient xxlJobOkHttpClient
    ) {
        XxlJobAdminWebClient xxlJobAdminWebClient = new XxlJobAdminWebClient();
        xxlJobAdminWebClient.setUsername(username);
        xxlJobAdminWebClient.setPassword(password);
        xxlJobAdminWebClient.setAdminAddress(address);
        xxlJobAdminWebClient.setXxlJobOkHttpClient(xxlJobOkHttpClient);
        return xxlJobAdminWebClient;
    }

    /**
     * 执行器
     *
     * @param applicationName
     * @param address
     * @param accessToken
     * @param xxlJobAdminWebClient
     * @return
     */
    @Bean
    public XxlJobAutoRegisterExecutor xxlJobExecutor(
            @Value("${spring.application.name}") String applicationName,
            @Value("${xxl-job.web.address:http://127.0.0.1:8080/xxl-job-admin}") String address,
            @Value("${xxl-job.web.accessToken:default_token}") String accessToken,
            @Value("${xxl-job.web.port:9999}") int xxlPort,
            @Autowired XxlJobAdminWebClient xxlJobAdminWebClient
    ) {
        XxlJobAutoRegisterExecutor executor = new XxlJobAutoRegisterExecutor();
        executor.setAdminAddresses(address);
        executor.setAppname(applicationName);
        executor.setPort(xxlPort);
        executor.setAccessToken(accessToken);
        executor.setLogPath("/data/java/logs/xxl-job/jobhandler");
        executor.setLogRetentionDays(30);
        executor.setXxlJobAdminWebClient(xxlJobAdminWebClient);
        return executor;
    }

}
