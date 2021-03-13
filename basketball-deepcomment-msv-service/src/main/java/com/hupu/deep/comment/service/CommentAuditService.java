package com.hupu.deep.comment.service;

import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.client.MachineAuditClient;
import com.hupu.deep.comment.client.UserClient;
import com.hupu.deep.comment.client.dto.MachineAuditResult;
import com.hupu.deep.comment.client.mq.MqClient;
import com.hupu.deep.comment.dal.CommentRecordMapper;
import com.hupu.deep.comment.dal.CommentSubjectMapper;
import com.hupu.deep.comment.dal.invertIndex.UserCommentRecordMapper;
import com.hupu.deep.comment.entity.CommentRecordDO;
import com.hupu.deep.comment.entity.CommentSubjectDO;
import com.hupu.deep.comment.enums.AuditStatusEnum;
import com.hupu.deep.comment.types.CommentKey;
import com.hupu.deep.comment.util.MqEventCode;
import com.hupu.foundation.util.AssertUtil;
import com.hupu.foundation.util.DateUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 评论审核服务 目前无状态机校验机制 完全以信息治理为准
 *
 * @author jiangfangyuan
 * @since 2020-03-13 11:10
 */
@Service
@Slf4j
public class CommentAuditService {


    @Autowired
    private UserCommentRecordMapper userCommentRecordMapper;

    @Autowired
    private CommentRecordMapper commentRecordMapper;

    @Autowired
    private CommentSubjectMapper commentSubjectMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private MqClient mqClient;


    @Autowired
    private ConfigClient configClient;

    @Autowired
    private MachineAuditClient machineAuditClient;


    @Autowired
    private ProducerBean manualAuditProducerBean;



    /**
     * 机器审核
     * //live_stream_danmaku
     *
     * @param commentKey
     */
    public MachineAuditResult machineAudit(CommentKey commentKey) {
        CommentRecordDO commentRecordDO = commentRecordMapper.selectByCommentId(commentKey.getSubjectId(), commentKey.getCommentId());
        AssertUtil.notNull(commentRecordDO, () -> "commentRecordDO null " + commentKey);
        CommentSubjectDO commentSubjectDO = commentSubjectMapper.selectBySubjectId(commentRecordDO.getSubjectId());
        return machineAuditClient.machineAudit(commentSubjectDO.getOutBizType(), commentRecordDO.getCommentContent());
    }

    /**
     * 人工审核通过
     *
     * @param commentKey
     */
    public void manualAuditPass(CommentKey commentKey, String auditUserName) {
        mqClient.sendTransactionMsg(configClient.getCommentTopic(), MqEventCode.EC_AUDIT_COMMENT, commentKey,
                (msg, arg) -> {
                    commentRecordMapper.updateManualAuditStatus(commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.AUDIT_PASS.getCode(), auditUserName);
                    commentRecordMapper.updateFinalAuditStatus(commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.AUDIT_PASS.getCode());
                    return TransactionStatus.CommitTransaction;
                }, commentKey);
    }

    /**
     * 人工审核不通过
     *
     * @param commentKey
     */
    @Transactional(rollbackFor = Exception.class)
    public void manualAuditNotPass(CommentKey commentKey, String auditUserName) {

        mqClient.sendTransactionMsg(configClient.getCommentTopic(), MqEventCode.EC_AUDIT_COMMENT, commentKey,
                (msg, arg) -> {
                    commentRecordMapper.updateManualAuditStatus(commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.AUDIT_NOT_PASS.getCode(), auditUserName);
                    commentRecordMapper.updateFinalAuditStatus(commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.AUDIT_NOT_PASS.getCode());
                    return TransactionStatus.CommitTransaction;
                }, commentKey);


    }

    /**
     * 人工审核 删除评论
     *
     * @param commentKey
     */
    @Transactional(rollbackFor = Exception.class)
    public void manualAuditDeleteComment(CommentKey commentKey, String auditUserName) {

        mqClient.sendTransactionMsg(configClient.getCommentTopic(), MqEventCode.EC_AUDIT_COMMENT, commentKey,
                (msg, arg) -> {
                    commentRecordMapper.updateManualAuditStatus(commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.DELETE.getCode(), auditUserName);
                    commentRecordMapper.updateFinalAuditStatus(commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.DELETE.getCode());
                    return TransactionStatus.CommitTransaction;
                }, commentKey);
    }


    /**
     * 人工审核 删除评论
     *
     * @param userId
     */
    @SneakyThrows
    public void manualAuditDeleteComment(Long userId) {

        if (Objects.isNull(userId)) {
            return;
        }
        while (true) {
            List<CommentRecordDO> commentRecordDOList =
                    userCommentRecordMapper.commentRecordList(userId, Lists.newArrayList(
                            AuditStatusEnum.WAITING_AUDIT.getCode(),
                            AuditStatusEnum.AUDIT_PASS.getCode(),
                            AuditStatusEnum.AUDIT_NOT_PASS.getCode()), 1000);
            if (CollectionUtils.isEmpty(commentRecordDOList)) {
                return;
            }
            for (CommentRecordDO commentRecordDO : commentRecordDOList) {

                CommentKey commentKey = new CommentKey(commentRecordDO.getSubjectId(), commentRecordDO.getCommentId());

                userCommentRecordMapper.updateManualAuditStatus(userId, commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.DELETE.getCode(), null);
                userCommentRecordMapper.updateFinalAuditStatus(userId, commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.DELETE.getCode());

                commentRecordMapper.updateManualAuditStatus(commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.DELETE.getCode(), null);
                commentRecordMapper.updateFinalAuditStatus(commentKey.getSubjectId(), commentKey.getCommentId(), AuditStatusEnum.DELETE.getCode());
            }
            TimeUnit.MILLISECONDS.sleep(500);
        }

    }


    /**
     * 发起人工审核
     *
     * @param commentKey
     */
    public void postManualAudit(CommentKey commentKey) {

        CommentRecordDO commentRecordDO = commentRecordMapper.selectByCommentId(commentKey.getSubjectId(), commentKey.getCommentId());
        AssertUtil.notNull(commentRecordDO, () -> "评论不存在 commentKey:" + commentKey);

        ManualAuditReq manualAuditReq = new ManualAuditReq();
        {
            CommentSubjectDO commentSubjectDO = commentSubjectMapper.selectBySubjectId(commentRecordDO.getSubjectId());
            AssertUtil.notNull(commentSubjectDO, () -> "评论主体不存在" + commentRecordDO);
            Subject subject = new Subject();
            subject.setSubject_id(commentSubjectDO.getSubjectId());
            subject.setOut_biz_no(commentSubjectDO.getOutBizNo());
            subject.setOut_biz_type(commentSubjectDO.getOutBizType());
            manualAuditReq.setSubject(subject);
        }

        {
            if (StringUtils.isNotBlank(commentRecordDO.getParentCommentId())) {
                CommentRecordDO parentComment = commentRecordMapper.selectByCommentId(commentRecordDO.getSubjectId(), commentRecordDO
                        .getParentCommentId());
                AssertUtil.notNull(parentComment, () -> "父评论不存在" + commentRecordDO);
                Parent parent = new Parent();
                parent.setComment_id(parentComment.getCommentId());
                parent.setComment_content(parentComment.getCommentContent());
                manualAuditReq.setParent(parent);
            }
        }

        manualAuditReq.setComment_id(commentRecordDO.getCommentId());
        manualAuditReq.setComment_content(commentRecordDO.getCommentContent());

        manualAuditReq.setPuid(commentRecordDO.getUserId().toString());
        manualAuditReq.setNickname(userClient.getNickName(commentRecordDO.getUserId()));


        manualAuditReq.setMachine_audit_status(commentRecordDO.getMachineAuditStatus());
        manualAuditReq.setRequest_id(commentRecordDO.getMachineAuditRequestId());

        manualAuditReq.setCreate_dt(DateUtils.dateToString(commentRecordDO.getCreateDt(), DateUtils.LONG_FORMAT));
        manualAuditReq.setUpdate_dt(DateUtils.dateToString(commentRecordDO.getUpdateDt(), DateUtils.LONG_FORMAT));

        mqClient.sendMsg(configClient.getManualAuditTopic(), configClient.getManualAuditEventCode()
                , manualAuditReq, manualAuditProducerBean);
    }

    @Data
    private static class ManualAuditReq {

        private Subject subject;

        private Parent parent;

        private String comment_id;
        private String comment_content;

        private String puid;
        private String nickname;


        private String machine_audit_status;
        private String request_id;


        private String create_dt;
        private String update_dt;

    }

    @Data
    private static class Subject {

        private String subject_id;
        private String out_biz_no;
        private String out_biz_type;
    }

    @Data
    private static class Parent {

        private String comment_id;
        private String comment_content;
    }


}
