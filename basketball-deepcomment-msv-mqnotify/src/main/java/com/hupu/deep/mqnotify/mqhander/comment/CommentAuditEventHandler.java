package com.hupu.deep.mqnotify.mqhander.comment;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.hupu.deep.comment.dal.CommentRecordMapper;
import com.hupu.deep.comment.dal.invertIndex.UserCommentRecordMapper;
import com.hupu.deep.comment.entity.CommentRecordDO;
import com.hupu.deep.comment.types.CommentKey;
import com.hupu.deep.mqnotify.mqhander.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

/**
 * @author jiangfangyuan
 * @since 2020-03-14 03:15
 */
@Component
@Slf4j
public class CommentAuditEventHandler implements EventHandler {

    @Autowired
    private UserCommentRecordMapper userCommentRecordMapper;

    @Autowired
    private CommentRecordMapper commentRecordMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Override
    public Action handleMsg(Message message) {

        CommentKey commentKey = JSON.parseObject(message.getBody(), CommentKey.class);
        String subjectId = commentKey.getSubjectId();
        String commentId = commentKey.getCommentId();

        log.info("开始维护反向索引表 信息治理审核状态 {}", commentId);

        transactionTemplate.execute(transactionStatus -> {

            //锁定防并发更新
            CommentRecordDO lockCommentRecord = commentRecordMapper.selectByCommentIdForUpdate(subjectId, commentId);
            if (Objects.isNull(lockCommentRecord)) {
                log.warn("评论不存在 {}", commentKey);
                return true;
            }

            userCommentRecordMapper.updateManualAuditStatus(lockCommentRecord.getUserId(), subjectId, commentId, lockCommentRecord.getManualAuditStatus(),lockCommentRecord.getAuditUserName());
            userCommentRecordMapper.updateFinalAuditStatus(lockCommentRecord.getUserId(), subjectId, commentId, lockCommentRecord.getFinalAuditStatus());

            return true;
        });
        return Action.CommitMessage;
    }
}
