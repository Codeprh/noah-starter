package com.noah.starter.mysql.interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.noah.starter.mysql.offset.NoahPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * mybatis-plus，interceptor管理
 */
@Slf4j
@Configuration
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@ConditionalOnProperty(prefix = "noah.mybatis-plus.interceptors", name = "enable", havingValue = "true", matchIfMissing = true)
public class MybatisPlusInterceptorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(NoahPage.class)
    @ConditionalOnProperty(prefix = "noah.mybatis-plus", name = "pagination.use", havingValue = "true", matchIfMissing = true)
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor(DbType.MYSQL);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "noah.mybatis-plus", name = "blockAttack.use", havingValue = "true", matchIfMissing = true)
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new BlockAttackInnerInterceptor();
    }

    //@Value("${spring.datasource.dynamic.datasource.master.url}")
    //private String jdbcUrl;

    //@Bean
    //@ConditionalOnProperty(prefix = "noah.mybatis-plus", name = "catMybatis.use", havingValue = "true", matchIfMissing = true)
    //public String CatMybatisPlusInterceptor(@Autowired(required = false) SqlSessionFactory sqlSessionFactory) {
    //    CatMybatisInterceptor catMybatisInterceptor = new CatMybatisInterceptor(jdbcUrl);
    //    if (sqlSessionFactory != null) {
    //        log.info("MybatisPlusInterceptorAutoConfiguration activate CatMybatisInterceptor");
    //        sqlSessionFactory.getConfiguration().addInterceptor(catMybatisInterceptor);
    //    }
    //    return "OK";
    //}

    //@Bean
    //public SqlSessionFactory mysqlSessionFactory(DataSource mysqlDataSource) throws Exception {
    //    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    //    sqlSessionFactoryBean.setDataSource(mysqlDataSource);
    //    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    //    sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:com/noah/*.xml"));
    //    sqlSessionFactoryBean.setPlugins(new Interceptor[]{new CatMybatisInterceptor(jdbcUrl)});
    //    return sqlSessionFactoryBean.getObject();
    //}

    //@Bean
    //public ConfigurationCustomizer mybatisConfigurationCustomizer() {
    //    return new ConfigurationCustomizer() {
    //        @Override
    //        public void customize(MybatisConfiguration configuration) {
    //            configuration.addInterceptor(new CatMybatisInterceptor(datasourceUrl));
    //        }
    //    };
    //}

    /**
     * 官方：新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false,
     * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    @Bean
    public MybatisPlusInterceptorBeanPostProcessor mybatisPlusInterceptorBeanPostProcessor(List<? extends InnerInterceptor> innerInterceptorList) {
        return new MybatisPlusInterceptorBeanPostProcessor(innerInterceptorList);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MybatisPlusInterceptorBeanPostProcessor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        return new MybatisPlusInterceptor();
    }

}
