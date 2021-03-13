package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.BaseTest;
import com.hupu.deep.comment.dal.invertIndex.UserCommentRecordMapper;
import com.hupu.deep.comment.entity.CommentRecordDO;
import com.hupu.deep.comment.enums.AuditStatusEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhuwenkang
 * @Time 2020年03月12日 13:48:00
 */
public class UserCommentRecordMapperTest extends BaseTest {

    @Autowired
    private UserCommentRecordMapper userCommentRecordMapper;

    @Test
    public void insertTest() {
        CommentRecordDO commentRecordDO = new CommentRecordDO();

        commentRecordDO.setSubjectId("12312");
        commentRecordDO.setCommentId("21312");
        commentRecordDO.setUserId(213123L);
        commentRecordDO.setParentCommentId("2323");
        commentRecordDO.setCommentContent("habvsdhjjasd");
        commentRecordDO.setFinalAuditStatus(AuditStatusEnum.WAITING_AUDIT.getCode());

        userCommentRecordMapper.insert(commentRecordDO);

    }

    @Test
    public void updateStatusTest() {
    }
}
