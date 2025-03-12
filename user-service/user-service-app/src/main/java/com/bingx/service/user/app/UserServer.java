package com.bingx.service.user.app;

import bon.framework.boot.autoconfigure.monitor.AsyncAyWeixinNotify;
import bon.framework.boot.autoconfigure.sns.annotation.client.EnableSnsClient;
import com.bon.framework.boot.core.log.LogNames;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author till
 */
@SpringBootApplication(scanBasePackages = {"com.bingx.service.user"})
@Configuration
@EnableCaching
@MapperScan("com.bingx.service.user.app.dao.mapper")
@EnableFeignClients(basePackages = {"com.bon.base","com.bingx.service.user"})
@EnableTransactionManagement
@EnableSwagger2
@EnableSnsClient(basePackages = {"com.bingx.service.user.app.sns"})
@ConfigurationPropertiesScan(basePackages = {"com.bingx.service.user.app.properties"})
public class UserServer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(LogNames.SERVER);

    public static void main(String[] args) {
        SpringApplication.run(UserServer.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("{} start success", UserServer.class.getSimpleName());
        AsyncAyWeixinNotify.send("{} start success!", UserServer.class.getSimpleName());
    }
}
