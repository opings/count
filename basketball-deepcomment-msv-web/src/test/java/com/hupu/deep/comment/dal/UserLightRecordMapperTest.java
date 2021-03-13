package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.BaseTest;
import com.hupu.deep.comment.DeepCommentApplication;
import com.hupu.deep.comment.dal.invertIndex.UserLightRecordMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 00:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeepCommentApplication.class)
public class UserLightRecordMapperTest extends BaseTest {

    @Autowired
    private UserLightRecordMapper userLightRecordMapper;

    @Test
    public void getLightRecord(){
        userLightRecordMapper.getLightRecord(11L,"11");
    }

}
