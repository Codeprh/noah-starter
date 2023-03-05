package com.noah.starter.apollo.meta;

import com.ctrip.framework.foundation.internals.DefaultProviderManager;
import com.ctrip.framework.foundation.spi.provider.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * spi注册器：
 * <p>
 * ApplicationProvider
 * NetworkProvider
 * Provider
 * ServerProvider
 */
@Slf4j
public class NoahProviderManager extends DefaultProviderManager {
    public NoahProviderManager() {

        //spring cloud zuul的路由规则:可以开启OrderedProperties特性来使得内存中的配置顺序和页面上看到的一致
        System.setProperty("apollo.property.order.enable", "true");

        // Load per-application configuration, like app id, from classpath://META-INF/app.properties
        Provider applicationProvider = new NoahApplicationProvider();
        applicationProvider.initialize();
        register(applicationProvider);

        //Load environment
        Provider serverProvider = new NoahServerProvider();
        serverProvider.initialize();
        register(serverProvider);

    }
}
