package com.noah.starter.apollo.meta;

import com.ctrip.framework.foundation.internals.provider.DefaultApplicationProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * appId，减少配置
 * 1、兼容旧配置
 */
@Slf4j
public class NoahApplicationProvider extends DefaultApplicationProvider {

    private volatile String mAppId;

    @Override
    public String getAppId() {

        String appId = super.getAppId();

        if (StringUtils.isNotBlank(appId)) {
            log.info("NoahApplicationProvider config appId:{}", appId);
            return appId;
        }

        if (StringUtils.isNotBlank(mAppId)) {
            log.info("NoahApplicationProvider guess appId:{}", mAppId);
            return mAppId;
        }

        mAppId = getSpringApplicationName();
        setmAppId(mAppId);

        log.info("NoahApplicationProvider set appId by spring.application.name:{}", mAppId);

        if (StringUtils.isNotBlank(mAppId)) {
            return mAppId;
        }

        return super.getAppId();

    }

    public synchronized void setmAppId(String mAppId) {
        this.mAppId = mAppId;
    }

    @SneakyThrows
    public String getSpringApplicationName() {

        InputStream in = ClassLoader.getSystemResourceAsStream("application.properties");

        Properties properties = new Properties();
        properties.load(in);

        String property = properties.getProperty("spring.application.name", null);
        return property;
    }


}
