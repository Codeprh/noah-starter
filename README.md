

## Noah-Starter

Use Spring, send Java，逃）

### What is this project

整理开发过程中觉得好用的插件或功能，制作成`starter`以方便以后开发`spring-boot`项目

|           模块           | 描述                                                     |
| :----------------------: | :------------------------------------------------------- |
| apollo-spring-boot-start | 减少每个项目配置繁琐的apollo.meta、appId、env等变量      |
|  cat-spring-boot-start   | 链路追踪组件，配置对应中间件的cat链路信息（TODO）        |
| mysql-spring-boot-start  | mysql-plus组件：动态数据源、各种拦截器：分页、空白更新等 |
|  web-spring-boot-start   | 业务和工具类的最佳实践，后续继续补充                     |
|  xxl-spring-boot-start   | 主动注册执行器和定时任务，无需配置                       |
|                          |                                                          |
|                          |                                                          |

### How to use it

```properties
spring.application.name=noah-demo-web
server.port=9091
spring.profiles.include=apollo
spring.datasource.dynamic.datasource.master.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.dynamic.datasource.master.url=jdbc:mysql://127.0.0.1:3306/noah?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai&useAffectedRows=true
spring.datasource.dynamic.datasource.master.username=root
spring.datasource.dynamic.datasource.master.password=xxxx
spring.datasource.dynamic.datasource.master.hikari.pool-name=t-pool
spring.datasource.dynamic.datasource.master.hikari.minimum-idle=10
spring.datasource.dynamic.datasource.master.hikari.max-lifetime=300000
spring.datasource.dynamic.datasource.master.hikari.idle-timeout=60000
noah.mybatis-plus.interceptors.enable=true
noah.mybatis-plus.pagination.use=true
noah.mybatis-plus.blockAttack.use=true
#spring.datasource.dynamic.enabled=false
noah.xxl-job.register.enable=false
```

### xxl-spring-boot-start

1. 第一步：只需要在依赖中加入pom依赖即可。

```xml
<dependency>
  <groupId>com.noah.starter</groupId>
  <artifactId>xxl-spring-boot-start</artifactId>
  <version>1.0.0</version>
</dependency>
```

2. 第二步：在类或者方法上加上`@XxlJobAutoRegister`注解即可，无需要管理后台手动注册任务

实现原理：

- 执行器注册

```
    /**
     * 执行器注册
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

```

- 定时任务注册

```java
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
```

### 未完待续

<p align="center">  
  <a href="https://github.com/Codeprh/noah-starter">
    <img alt="cayzlh-starters version" src="https://img.shields.io/badge/noah--starter-1.0.x-blue">
  </a>
  <a href="https://github.com/spring-projects/spring-boot">
    <img alt="spring boot version" src="https://img.shields.io/badge/spring%20boot-2.2.1.RELEASE-brightgreen">
  </a>
  <a href="https://github.com/Codeprh/noah-starter/blob/main/LICENSE">
    <img alt="code style" src="https://img.shields.io/github/license/cayzlh/noah-starter">
  </a>
</p>
