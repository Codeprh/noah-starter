package com.noah.starter.apollo.meta;

import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.internals.DefaultMetaServerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Apollo Meta Server地址定位逻辑
 * 1、无需配置apollo.meta
 * 2、兼容配置的方式
 */
@Slf4j
public class NoahMetaServerProvider extends DefaultMetaServerProvider {

    public NoahMetaServerProvider() {
        log.info("NoahMetaServerProvider running");
    }

    @Override
    public String getMetaServerAddress(Env targetEnv) {

        String metaServerAddress = super.getMetaServerAddress(targetEnv);

        if (StringUtils.isNotBlank(metaServerAddress)) {
            log.info("NoahMetaServerProvider use config metaServerAddress:{}", metaServerAddress);
            return metaServerAddress;
        }

        if (Env.DEV.equals(targetEnv)) {
            metaServerAddress = "http://apollo-service-apollo-configservice:8080";
        }

        if (Env.PRO.equals(targetEnv)) {
            metaServerAddress = "http://apollo-service-apollo-configservice:8080";
        }

        log.info("NoahMetaServerProvider guess env:{},metaServerAddress:{}", targetEnv.name(), metaServerAddress);

        return metaServerAddress;
    }

    @Override
    public int getOrder() {
        return super.getOrder() - 1;
    }
}
