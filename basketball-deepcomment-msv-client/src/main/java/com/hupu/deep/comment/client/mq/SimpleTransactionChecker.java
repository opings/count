package com.hupu.deep.comment.client.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.dal.CommentRecordMapper;
import com.hupu.deep.comment.dal.invertIndex.UserLightRecordMapper;
import com.hupu.deep.comment.dal.invertIndex.UserScoreRecordMapper;
import com.hupu.deep.comment.entity.CommentRecordDO;
import com.hupu.deep.comment.entity.LightRecordDO;
import com.hupu.deep.comment.entity.ScoreRecordDO;
import com.hupu.deep.comment.types.CommentKey;
import com.hupu.deep.comment.util.MqEventCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 13:58
 */
@Service
@Slf4j
public class SimpleTransactionChecker implements LocalTransactionChecker {

    @Autowired
    private MqProducerConfig mqProducerConfig;

    @Autowired
    private ConfigClient configClient;

    @Autowired
    private CommentRecordMapper commentRecordMapper;

    @Autowired
    private UserLightRecordMapper userLightRecordMapper;

    @Autowired
    private UserScoreRecordMapper userScoreRecordMapper;


    @Override
    public TransactionStatus check(Message msg) {

        log.info("LocalTransactionChecker 消息回调 {}", msg);

        if (StringUtils.equalsIgnoreCase(msg.getTopic(), configClient.getCommentTopic())) {

            if (StringUtils.equalsIgnoreCase(msg.getTag(), MqEventCode.EC_PUBLISH_COMMENT)) {
                return Objects.nonNull(getCommentRecordDO(msg)) ? TransactionStatus.CommitTransaction : TransactionStatus.RollbackTransaction;
            }
            if (StringUtils.equalsIgnoreCase(msg.getTag(), MqEventCode.EC_AUDIT_COMMENT)) {
                return TransactionStatus.CommitTransaction;
            }
        }

        if (StringUtils.equalsIgnoreCase(msg.getTopic(), configClient.getLightTopic())) {

            if (StringUtils.equalsIgnoreCase(msg.getTag(), MqEventCode.EC_LIGHT)) {
                return Objects.nonNull(getUserLightRecordDO(msg)) ? TransactionStatus.CommitTransaction : TransactionStatus.RollbackTransaction;
            }
            if (StringUtils.equalsIgnoreCase(msg.getTag(), MqEventCode.EC_CANCEL_LIGHT)) {
                return TransactionStatus.CommitTransaction;
            }
        }

        if (StringUtils.equalsIgnoreCase(msg.getTopic(), configClient.getScoreTopic())) {

            if (StringUtils.equalsIgnoreCase(msg.getTag(), MqEventCode.EC_SCORE)) {
                return Objects.nonNull(getUserScoreRecordDO(msg)) ? TransactionStatus.CommitTransaction : TransactionStatus.RollbackTransaction;
            }
        }
        return TransactionStatus.RollbackTransaction;
    }

    private CommentRecordDO getCommentRecordDO(Message message) {
        CommentKey commentKey = JSON.parseObject(message.getBody(), CommentKey.class);
        return commentRecordMapper.selectByCommentId(commentKey.getSubjectId(), commentKey
                .getCommentId());
    }

    private LightRecordDO getUserLightRecordDO(Message message) {
        Map<String, Object> msgBodyMap = JSON.parseObject(new String(message.getBody()),
                new TypeReference<Map<String, Object>>() {
                });
        Long userId = Long.valueOf(msgBodyMap.get("userId").toString());
        String subjectId = msgBodyMap.get("subjectId").toString();
        return userLightRecordMapper.getLightRecord(userId, subjectId);
    }


    private ScoreRecordDO getUserScoreRecordDO(Message message) {
        Map<String, Object> msgBodyMap = JSON.parseObject(new String(message.getBody()),
                new TypeReference<Map<String, Object>>() {
                });
        Long userId = Long.valueOf(msgBodyMap.get("userId").toString());
        String subjectId = msgBodyMap.get("subjectId").toString();
        return userScoreRecordMapper.getScoreRecord(userId, subjectId);
    }
}
