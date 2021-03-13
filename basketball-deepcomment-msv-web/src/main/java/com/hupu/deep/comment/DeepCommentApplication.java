package com.hupu.deep.comment;

import com.hupu.basketball.framework.core.web.HPApplication;
import com.hupuarena.msvfoundation.annotation.EnableHupuMsv;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author lisongqiu
 */
@SpringBootApplication(scanBasePackages = {"com.hupu.deep", "com.hupu.foundation"})
@EnableHupuMsv
@EnableTransactionManagement
@EnableAspectJAutoProxy
@MapperScan("com.hupu.deep.comment.dal")
@EnableFeignClients(basePackages = { "com.hupu.deep.comment.client.feign"})
public class DeepCommentApplication {
    public static void main(String[] args) {
        HPApplication.start(DeepCommentApplication.class, args);
    }
}

