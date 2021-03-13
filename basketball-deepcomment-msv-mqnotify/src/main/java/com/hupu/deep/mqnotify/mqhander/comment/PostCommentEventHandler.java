package com.hupu.deep.mqnotify.mqhander.comment;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.client.dto.MachineAuditResult;
import com.hupu.deep.comment.dal.CommentRecordMapper;
import com.hupu.deep.comment.dal.invertIndex.UserCommentRecordMapper;
import com.hupu.deep.comment.entity.CommentRecordDO;
import com.hupu.deep.comment.enums.AuditStatusEnum;
import com.hupu.deep.comment.service.CommentAuditService;
import com.hupu.deep.comment.service.CommentSubjectService;
import com.hupu.deep.comment.types.CommentKey;
import com.hupu.deep.mqnotify.mqhander.EventHandler;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 发布评论后置处理
 *
 * @author zhuwenkang
 * @Time 2020年03月12日 17:07:00
 */
@Component
@Slf4j
public class PostCommentEventHandler implements EventHandler {

    @Autowired
    private UserCommentRecordMapper userCommentRecordMapper;

    @Autowired
    private CommentRecordMapper commentRecordMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CommentAuditService commentAuditService;

    @Autowired
    private CommentSubjectService commentSubjectService;

    @Autowired
    private ConfigClient configClient;

    @Override
    public Action handleMsg(Message message) {

        CommentRecordDO commentRecordDO = getCommentRecordDO(message);
        if (commentRecordDO == null) {
            return Action.CommitMessage;
        }

        Long userId = commentRecordDO.getUserId();
        CommentKey commentKey = new CommentKey(commentRecordDO.getSubjectId(), commentRecordDO.getCommentId());

        log.info("开始对发布消息 后置处理 {}", commentKey);

        //1 维护反向索引表
        log.info("维护反向索引表 {}", commentKey);
        CommentRecordDO userCommentRecord = userCommentRecordMapper.selectByCommentId(commentRecordDO.getUserId(), commentRecordDO.getSubjectId(), commentRecordDO.getCommentId());
        if (null == userCommentRecord) {
            try {
                userCommentRecordMapper.insert(commentRecordDO);
            } catch (DuplicateKeyException ex) {
                log.info("发布评论 消息重发 comment {}", commentRecordDO);
            }
        }

        //2 机器审核
        log.info("发起机器审核 {}", commentKey);
        MachineAuditResult auditResult = commentAuditService.machineAudit(commentKey);
        if (auditResult.isSuccess()) {

            String machineAuditStatus = auditResult.isPass() ? AuditStatusEnum.AUDIT_PASS.getCode() :
                    AuditStatusEnum.AUDIT_NOT_PASS.getCode();
            transactionTemplate.execute(transactionStatus -> {

                String subjectId = commentKey.getSubjectId();
                String commentId = commentKey.getCommentId();

                //锁定防并发更新 机器审核结果
                CommentRecordDO lockCommentRecord = commentRecordMapper.selectByCommentIdForUpdate(subjectId, commentId);
                AssertUtil.notNull(lockCommentRecord, () -> "评论不存在" + commentKey);

                log.info("机器审核结果 subjectId:{},commentId:{},machineAuditStatus:{}",subjectId,commentId, machineAuditStatus);
                commentRecordMapper.updateMachineAuditStatus(subjectId, commentId, machineAuditStatus);
                commentRecordMapper.updateMachineAuditRequestId(subjectId, commentId, auditResult.getRequestId());

                userCommentRecordMapper.updateMachineAuditStatus(userId, subjectId, commentId, machineAuditStatus);

                if (StringUtils.equalsIgnoreCase(lockCommentRecord.getFinalAuditStatus(), AuditStatusEnum.WAITING_AUDIT.getCode())) {
                    commentRecordMapper.updateFinalAuditStatus(subjectId, commentId, machineAuditStatus);
                    userCommentRecordMapper.updateFinalAuditStatus(userId, subjectId, commentId, machineAuditStatus);
                }

                return true;
            });
        }

//        //3 发起人工审核
//        log.info("发起人工审核 {}", commentKey);
//        commentAuditService.postManualAudit(commentKey);
        return Action.CommitMessage;
    }

    private CommentRecordDO getCommentRecordDO(Message message) {
        CommentKey commentKey = JSON.parseObject(message.getBody(), CommentKey.class);
        return commentRecordMapper.selectByCommentId(commentKey.getSubjectId(), commentKey
                .getCommentId());
    }
}
