package com.hupu.deep.comment;

import com.hupu.deep.comment.client.ProhibitionClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jiangfangyuan
 * @since 2020-03-20 18:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeepCommentApplication.class)
public class ClientTest extends BaseTest {

    @Autowired
    private ProhibitionClient prohibitionClient;

    @Test
    public void isUserProhibition() {
        prohibitionClient.isUserProhibition(11L, "discussPlayer", 22L);
    }



}
