package com.hupu.deep.comment;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 00:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeepCommentApplication.class)
public class BaseTest {

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("env", "dev");
        System.setProperty("app.id", "basketball-deepcomment-msv");
        System.setProperty("dev_meta", "http://192.168.11.45:8080");
        System.setProperty("spring.application.name", "basketball-deepcomment-msv");
        System.setProperty("spring.profiles.active", "test");
        System.setProperty("mybatis.configuration.log-impl", "org.apache.ibatis.logging.stdout.StdOutImpl");
    }


}
