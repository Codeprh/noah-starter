package com.noah.starter.apollo.meta;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.noah.starter.apollo.log.NoahLoggerLevelRefresh;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@EnableApolloConfig
public class NoahApolloAutoConfiguration {
    @Bean
    public NoahLoggerLevelRefresh noahLoggerLevelRefresh() {
        return new NoahLoggerLevelRefresh();
    }
}
