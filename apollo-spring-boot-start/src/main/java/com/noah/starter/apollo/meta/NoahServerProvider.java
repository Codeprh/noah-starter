package com.noah.starter.apollo.meta;

import cn.hutool.core.io.FileUtil;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.foundation.internals.NetworkInterfaceManager;
import com.ctrip.framework.foundation.internals.provider.DefaultServerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * env环境变量设置
 * 1、移除/opt/settings/server.properties
 * 2、并且支持文件和环境变量的设置
 */
@Slf4j
public class NoahServerProvider extends DefaultServerProvider {

    private String m_env;

    @Override
    public String getEnvType() {

        if (super.isEnvTypeSet()) {
            return super.getEnvType();
        }

        if (StringUtils.isNotBlank(m_env)) {
            return m_env;
        }

        log.info("NoahServerProvider no env config");

        //判断是否在容器中
        File dockerFile = new File("/.dockerenv");
        boolean runDocker = FileUtil.isNotEmpty(dockerFile);

        String localHostAddress = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();
        String localHostName = NetworkInterfaceManager.INSTANCE.getLocalHostName();

        if (runDocker) {

            log.info("NoahServerProvider run docker,localHostAddress:{},localHostName:{}", localHostAddress, localHostName);
            if (localHostAddress.startsWith("10.42") || localHostAddress.startsWith("127.0")) {
                m_env = Env.DEV.name();
            }

            if (localHostAddress.startsWith("10.216")) {
                m_env = Env.PRO.name();
            }
        }

        //公司本地运行
        if (localHostAddress.startsWith("10.252")) {
            m_env = Env.DEV.name();
        }

        if (StringUtils.isNotBlank(m_env)) {
            log.info("NoahServerProvider init env:{}", m_env);
            return m_env;
        }

        //兜底还是走apollo的逻辑
        return super.getEnvType();
    }
}
