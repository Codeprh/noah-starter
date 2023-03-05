package com.noah.starter.mysql.interceptor;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * mybatis-插件,后置处理器
 */
@Slf4j
public class MybatisPlusInterceptorBeanPostProcessor implements BeanPostProcessor {
    @Setter
    private List<? extends InnerInterceptor> interceptors = new ArrayList<>();

    public MybatisPlusInterceptorBeanPostProcessor(List<? extends InnerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //log.info("i am postProcessBeforeInitialization bean:{}", beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof MybatisPlusInterceptor) {

            MybatisPlusInterceptor interceptor = (MybatisPlusInterceptor) bean;
            Set<? extends Class<? extends InnerInterceptor>> originalInnerInterceptor = interceptor.getInterceptors().stream().map(InnerInterceptor::getClass).collect(Collectors.toSet());

            interceptors.stream().filter(i -> !originalInnerInterceptor.contains(i)).forEach(i -> {
                log.info("MybatisPlusInterceptorBeanPostProcessor use new Interceptor:{}", i);
                interceptor.addInnerInterceptor(i);
            });

        }

        return bean;
    }
}
