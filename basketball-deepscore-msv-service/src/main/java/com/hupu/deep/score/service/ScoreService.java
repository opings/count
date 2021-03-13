package com.hupu.deep.score.service;

import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.google.common.collect.Maps;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.client.mq.MqClient;
import com.hupu.deep.comment.dal.invertIndex.UserScoreRecordMapper;
import com.hupu.deep.comment.entity.ScoreRecordDO;
import com.hupu.deep.comment.enums.OutBizTypeEnum;
import com.hupu.deep.comment.types.ScoreKey;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.comment.util.MqEventCode;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author yuepenglei
 * @Description 打分逻辑API
 * @Date 2020/03/13 13:39
 */

@Service
@Slf4j
public class ScoreService {

    @Autowired
    private MqClient mqClient;

    @Autowired
    private ConfigClient configClient;

    @Autowired
    private UserScoreRecordMapper userScoreRecordMapper;

    @Autowired
    private IdService idService;


    /**
     * 打分
     *
     * @param subjectId 主体ID
     * @param userId    用户ID
     * @param score     分数
     */
    public ScoreKey saveScore(String subjectId, Long userId, Integer score) {
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notNull(score, () -> "score null");
        AssertUtil.isTrue(score > Globals.NUM_0, () -> "评分必须>0");

        Map<String, Object> msgBody = Maps.newHashMap();
        msgBody.put("subjectId", subjectId);
        msgBody.put("userId", userId);

        mqClient.sendTransactionMsg(configClient.getScoreTopic(), MqEventCode.EC_SCORE, msgBody, (msg, arg) -> {
            try {
                if (Objects.isNull(userScoreRecordMapper.getScoreRecord(userId, subjectId))) {
                    try {
                        ScoreRecordDO scoreRecord = new ScoreRecordDO();
                        scoreRecord.setUserId(userId);
                        scoreRecord.setSubjectId(subjectId);
                        scoreRecord.setScoreRecordId(idService.genId(OutBizTypeEnum.SCORE.getCode(), userId, Globals.DB_SIZE).toString());
                        scoreRecord.setScore(score);
                        userScoreRecordMapper.saveScoreRecord(scoreRecord);
                        return TransactionStatus.CommitTransaction;
                    } catch (DuplicateKeyException ex) {
                        log.warn("userId {} 对 {} 重复评分", userId, subjectId);
                    }
                }
                userScoreRecordMapper.updateScoreRecord(userId, subjectId, score);
                return TransactionStatus.CommitTransaction;
            } catch (Exception ex) {
                log.error("score error userId" + userId + " subjectId" + subjectId, ex);
                return TransactionStatus.RollbackTransaction;
            }
        }, msgBody);
        ScoreRecordDO scoreRecordDO = userScoreRecordMapper.getScoreRecord(userId, subjectId);
        return Optional.ofNullable(scoreRecordDO)
                .map(item -> new ScoreKey(item.getSubjectId(), item.getScoreRecordId()))
                .orElse(null);

    }
}
