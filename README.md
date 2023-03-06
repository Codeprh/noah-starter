

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

<p align="center">  
  <a href="https://github.com/Codeprh/noah-starter">
    <img alt="cayzlh-starters version" src="https://img.shields.io/badge/noah--starter-1.0.x-blue">
  </a>
  <a href="https://github.com/spring-projects/spring-boot">
    <img alt="spring boot version" src="https://img.shields.io/badge/spring%20boot-2.2.1.RELEASE-brightgreen">
  </a>
  <a href="https://github.com/cayzlh/noah-starter/blob/master/LICENSE">
    <img alt="code style" src="https://img.shields.io/github/license/cayzlh/noah-starter">
  </a>
</p>
