package com.noah.starter.xxl.meta;

import com.noah.starter.xxl.annotation.XxlJobAutoRegister;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public class XxlJobAutoRegisterExecutor extends XxlJobExecutor implements BeanPostProcessor, SmartInitializingSingleton, DisposableBean {

    @Setter
    @Getter
    private XxlJobAdminWebClient xxlJobAdminWebClient;

    private String appname;

    private Long jobGroupId;

    @Override
    public void setAppname(String appname) {
        this.appname = appname;
        super.setAppname(appname);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("i am want to register xxljob");
        if (bean instanceof IJobHandler) {
            //类级别
            Optional<XxlJobAutoRegister> autoRegisterXxlJob = Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(bean.getClass(), XxlJobAutoRegister.class));
            autoRegistJobHandler(AopUtils.getTargetClass(bean).getName(), (IJobHandler) bean, autoRegisterXxlJob.map(XxlJobAutoRegister::remark).orElse(null), autoRegisterXxlJob.map(XxlJobAutoRegister::cron).orElse(null));
        } else {
            //方法
            Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(bean.getClass());
            for (Method method : methods) {
                XxlJobAutoRegister autoRegisterXxlJob = AnnotatedElementUtils.findMergedAnnotation(method, XxlJobAutoRegister.class);
                if (autoRegisterXxlJob == null) {
                    continue;
                }
                String name = AopUtils.getTargetClass(bean).getName() + "#" + method.getName();
                MethodJobHandler methodJobHandler = new MethodJobHandler(bean, method, null, null);
                autoRegistJobHandler(name, methodJobHandler, autoRegisterXxlJob.remark(), autoRegisterXxlJob.cron());
            }
        }
        return bean;
        //return null;
    }

    public void autoRegistJobHandler(String name, IJobHandler jobHandler, String remark, String cron) {
        registJobHandler(name, jobHandler);

        Long jobInfoId = xxlJobAdminWebClient.getJobInfoId(getJobGroupId(), name);
        if (jobInfoId == null) {
            log.info("定时任务[{}]不存在，新建一下", name);
            xxlJobAdminWebClient.saveJobInfo(getJobGroupId(), remark, cron, name);
        } else {
            log.info("定时任务[{}]已存在，跳过", name);
        }
    }

    public Long getJobGroupId() {
        jobGroupId = xxlJobAdminWebClient.getJobGroupId(appname);
        if (jobGroupId == null) {
            log.warn("xxl-job-admin上没有找到对应的执行器[{}]，新建一下", appname);
            xxlJobAdminWebClient.saveJobGroup(appname);
            jobGroupId = xxlJobAdminWebClient.getJobGroupId(appname);
            Validate.notNull(jobGroupId, "还是获取不到jobGroupId");
        }
        return jobGroupId;
    }

    @Override
    @SneakyThrows
    public void afterSingletonsInstantiated() {
        super.start();
    }
}
