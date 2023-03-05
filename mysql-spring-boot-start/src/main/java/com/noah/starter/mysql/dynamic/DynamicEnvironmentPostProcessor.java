package com.noah.starter.mysql.dynamic;

import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.*;

import java.util.Map;
import java.util.Objects;

@Order(Ordered.LOWEST_PRECEDENCE)
public class DynamicEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final String DYNAMIC_ENABLED_NAME = "DYNAMIC_ENABLED_NAME";
    private final String DYNAMIC_ENABLED = "spring.datasource.dynamic.enabled";
    private final String DYNAMIC_PREFIX = "spring.datasource.dynamic.datasource";

    private final Log log;

    /**
     * 使用这种方式是不会输出日志的，因为此时日志系统还为初始化，需要使用DeferredLog存日志，让后重放日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicEnvironmentPostProcessor.class);

    /**
     * 从SpringBoot2.4开始就可以注入DeferredLogFactory对象，可以使用这个来输出日志
     */
    public DynamicEnvironmentPostProcessor(DeferredLogFactory deferredLogFactory) {
        this.log = deferredLogFactory.getLog(DynamicEnvironmentPostProcessor.class);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        LOGGER.info("此日志无法输出");
        log.info("DynamicEnvironmentPostProcessor hello world");

        if (environment.getPropertySources().contains(DYNAMIC_ENABLED_NAME) || Objects.nonNull(environment.getProperty(DYNAMIC_ENABLED))) {
            log.info("DynamicEnvironmentPostProcessor already set value spring.datasource.dynamic.enabled = " + environment.getProperty(DYNAMIC_ENABLED));
            return;
        }

        MutablePropertySources propertySources = environment.getPropertySources();
        for (PropertySource<?> propertySource : propertySources) {

            if (propertySource instanceof EnumerablePropertySource) {
                EnumerablePropertySource source = (EnumerablePropertySource) propertySource;
                for (String propertyName : source.getPropertyNames()) {
                    boolean hasDynamic = propertyName.startsWith(DYNAMIC_PREFIX);
                    if (hasDynamic) {
                        Map<String, Object> properties = Maps.newHashMap();
                        properties.put(DYNAMIC_ENABLED, true);
                        environment.getPropertySources().addFirst(new MapPropertySource(DYNAMIC_ENABLED_NAME, properties));
                        log.info("DynamicEnvironmentPostProcessor set spring.datasource.dynamic.enabled = " + environment.getProperty(DYNAMIC_ENABLED));
                        break;
                    }
                }
            }
        }

        //不需要开启多数据源
        if (Objects.isNull(environment.getProperty(DYNAMIC_ENABLED))) {
            Map<String, Object> properties = Maps.newHashMap();
            properties.put(DYNAMIC_ENABLED, false);
            environment.getPropertySources().addFirst(new MapPropertySource(DYNAMIC_ENABLED_NAME, properties));
            log.info("DynamicEnvironmentPostProcessor set spring.datasource.dynamic.enabled = " + environment.getProperty(DYNAMIC_ENABLED));
        }


    }

}
