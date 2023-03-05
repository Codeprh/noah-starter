package com.noah.starter.demo.web.xxl;

import com.noah.starter.xxl.annotation.XxlJobAutoRegister;
import com.xxl.job.core.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@XxlJobAutoRegister(value = "这是类的任务value", remark = "这是类的任务remark", cron = "10 * * * * ?")
public class TypeUseXxl extends IJobHandler {

    @Override
    public void execute() throws Exception {
        log.info("hello 类job");
    }
}
