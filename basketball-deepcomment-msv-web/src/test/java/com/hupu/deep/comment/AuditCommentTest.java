package com.hupu.deep.comment;

import com.hupu.deep.comment.client.MachineAuditClient;
import com.hupu.deep.comment.client.dto.MachineAuditResult;
import com.hupu.deep.comment.service.CommentAuditService;
import com.hupu.deep.comment.types.CommentKey;
import com.hupu.deep.comment.util.Globals;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 16:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeepCommentApplication.class)
public class AuditCommentTest extends BaseTest {

    @Autowired
    private CommentAuditService commentAuditService;

    @Autowired
    private MachineAuditClient machineAuditClient;


    @Test
    public void machineAudit() {
        String appId = Globals.LIVE_STREAM_DANMAKU;
        String content = "你好";
        MachineAuditResult machineAuditResult = machineAuditClient.machineAudit(appId, content);
        Assert.assertTrue(machineAuditResult.isSuccess());
    }


    @Test
    public void manualAudit() {
        CommentKey commentKey = new CommentKey("689501758367465472", "2880011");
        commentAuditService.manualAuditPass(commentKey, "2222");
    }

}
