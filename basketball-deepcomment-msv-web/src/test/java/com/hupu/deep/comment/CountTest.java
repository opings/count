package com.hupu.deep.comment;

import com.google.common.collect.Lists;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.count.service.CountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author jiangfangyuan
 * @since 2020-03-14 00:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeepCommentApplication.class)
public class CountTest extends BaseTest {


    @Autowired
    private CountService countService;


    @Test
    public void count() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(10);


        List<Exception> exceptionList = Lists.newArrayList();

        IntStream.rangeClosed(1, 10000000).forEach(item -> {
            executorService.execute(() -> {
                try {
                    OutBizKey outBizKey = new OutBizKey("light", "111");
                    countService.publishOrUpdateCountFlow(outBizKey, 1, "22e34242");
                } catch (Exception ex) {
                    exceptionList.add(ex);
                }
            });
        });

        System.out.println("exceptionListexceptionListexceptionListexceptionList" + exceptionList);
        Thread.currentThread().join();

    }

}
