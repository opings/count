package com.hupu.deep.comment;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.google.common.collect.Lists;
import com.hupu.deep.comment.dal.CommentRecordMapper;
import com.hupu.deep.comment.entity.CommentRecordDO;
import com.hupu.deep.comment.enums.AuditStatusEnum;
import com.hupu.deep.comment.facade.CommentFacade;
import com.hupu.deep.comment.request.CommentListRequest;
import com.hupu.deep.comment.request.PublishCommentRequest;
import com.hupu.deep.comment.service.CommentService;
import com.hupu.deep.comment.service.CommentSubjectService;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.web.controller.CommentController;
import com.hupu.deep.mqnotify.mqhander.comment.PostCommentEventHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhuwenkang
 * @Time 2020年03月12日 17:48:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeepCommentApplication.class)
public class CommentTest extends BaseTest {

    @Autowired
    private CommentController commentController;


    @Autowired
    private CommentFacade commentFacade;

    @Autowired
    private CommentSubjectService commentSubjectService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRecordMapper commentRecordMapper;

    @Autowired
    private PostCommentEventHandler postCommentEventHandler;


    @Test
    public void publishComment() throws Exception {

        PublishCommentRequest commentRequest = new PublishCommentRequest();
        commentRequest.setUserId(11L);
        commentRequest.setOutBizKey(new OutBizKey("article", "98978"));
        commentRequest.setContent("睡吧睡吧睡吧不是吧");
        String commentId = commentController.publishComment(commentRequest).getResult().getCommentId();

        String subjectId = commentSubjectService.getSubjectId(new OutBizKey("article", "98978"));

        CommentRecordDO commentRecordDO = commentRecordMapper.selectByCommentId(subjectId, commentId);
        Message message = new Message();
        message.setBody(JSON.toJSONString(commentRecordDO).getBytes());
        postCommentEventHandler.handleMsg(message);
    }


    @Test
    public void commentList() {
        CommentListRequest request = new CommentListRequest();
        request.setOutBizKey(new OutBizKey("article", "98978"));
        request.setCommentStatusList(Lists.newArrayList(AuditStatusEnum.AUDIT_PASS.getCode()));
        commentFacade.commentList(request);
    }
}
