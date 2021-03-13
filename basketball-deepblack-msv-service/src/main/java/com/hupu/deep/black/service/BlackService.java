package com.hupu.deep.black.service;

import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.google.common.collect.Maps;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.client.mq.MqClient;
import com.hupu.deep.comment.dal.invertIndex.UserBlackRecordMapper;
import com.hupu.deep.comment.entity.BlackRecordDO;
import com.hupu.deep.comment.enums.BizTypeEnum;
import com.hupu.deep.comment.enums.BlackStatusEnum;
import com.hupu.deep.comment.model.BlackRecordModel;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.comment.util.MqEventCode;
import com.hupu.deep.comment.util.error.ErrorCode;
import com.hupu.deep.count.service.CountService;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.deep.black.helper.BlackHelper;
import com.hupu.foundation.error.CommonSystemException;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 13:21
 */
@Service
@Slf4j
public class BlackService {


    @Autowired
    private MqClient mqClient;

    @Autowired
    private UserBlackRecordMapper userBlackRecordMapper;

    @Autowired
    private BlackHelper blackHelper;

    @Autowired
    private ConfigClient configClient;

    @Autowired
    private CountService countService;

    @Autowired
    private IdService idService;


    /**
     * 点灭
     *
     * @param userId
     * @param subjectId
     */
    public void black(Long userId, String subjectId) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        BlackRecordDO userBlackRecordDO = userBlackRecordMapper.getBlackRecord(userId, subjectId);
        if (Objects.nonNull(userBlackRecordDO)) {
            if (StringUtils.equalsIgnoreCase(userBlackRecordDO.getBlackStatus(), BlackStatusEnum.BLACK.getCode())) {
                return;
            }
        }

        Map<String, Object> msgBody = Maps.newHashMap();
        msgBody.put("userId", userId);
        msgBody.put("subjectId", subjectId);

        mqClient.sendTransactionMsg(configClient.getBlackTopic(), MqEventCode.EC_BLACK, msgBody, (msg, arg) -> {
            try {
                if (null == userBlackRecordDO) {
                    try {
                        BlackRecordDO blackRecord = new BlackRecordDO();
                        blackRecord.setUserId(userId);
                        blackRecord.setSubjectId(subjectId);
                        blackRecord.setBlackRecordId(idService.genId(BizTypeEnum.BLACK.getCode(),userId, Globals.DB_SIZE).toString());
                        blackRecord.setBlackStatus(BlackStatusEnum.BLACK.getCode());
                        userBlackRecordMapper.saveBlackRecord(blackRecord);
                    } catch (DuplicateKeyException ex) {
                        log.warn("userId {} 对 {} 重复点灭", userId, subjectId);
                        userBlackRecordMapper.black(userId, subjectId);
                    }
                    return TransactionStatus.CommitTransaction;
                }
                userBlackRecordMapper.black(userId, subjectId);
                return TransactionStatus.CommitTransaction;
            } catch (Exception ex) {
                log.error("black error userId" + userId + " subjectId" + subjectId, ex);
                return TransactionStatus.RollbackTransaction;
            }
        }, msgBody);
    }


    /**
     * 取消点灭
     *
     * @param userId
     * @param subjectId
     */
    public void cancelBlack(Long userId, String subjectId) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        BlackRecordDO userBlackRecordDO = userBlackRecordMapper.getBlackRecord(userId, subjectId);
        if (null == userBlackRecordDO) {
            throw new CommonSystemException(ErrorCode.NOT_EXIST_BLACK_SUBJECT.getCode(),
                    ErrorCode.NOT_EXIST_BLACK_SUBJECT.getMsg()).putExtra("subjectId", subjectId)
                    .putExtra("userId", userId);
        }
        if (StringUtils.equalsIgnoreCase(userBlackRecordDO.getBlackStatus(), BlackStatusEnum.CANCEL_BLACK.getCode())) {
            return;
        }
        Map<String, Object> msgBody = Maps.newHashMap();
        msgBody.put("userId", userId);
        msgBody.put("subjectId", subjectId);

        mqClient.sendTransactionMsg(configClient.getBlackTopic(), MqEventCode.EC_CANCEL_BLACK, msgBody, (msg, arg) -> {
            try {
                userBlackRecordMapper.cancelBlack(userId, subjectId);
                return TransactionStatus.CommitTransaction;
            } catch (Exception ex) {
                log.error("cancelBlackInner error userId" + userId + " subjectId" + subjectId, ex);
                return TransactionStatus.RollbackTransaction;
            }
        }, msgBody);
    }

    /**
     * 查询某个用户是否对某个主体进行点灭过
     *
     * @param userId    userId
     * @param subjectId subjectId
     * @return 是否点灭
     */
    public boolean hasBlack(Long userId, String subjectId) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        BlackRecordDO blackRecordDO = userBlackRecordMapper.getBlackRecord(userId, subjectId);
        return Objects.nonNull(blackRecordDO) &&
                StringUtils.equalsIgnoreCase(blackRecordDO.getBlackStatus(), BlackStatusEnum.BLACK.getCode());
    }


    public BlackRecordModel getUserBlackRecordModel(Long userId, String subjectId) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        BlackRecordDO blackRecordDO = userBlackRecordMapper.getBlackRecord(userId, subjectId);
        return blackHelper.buildBlackRecordModel(blackRecordDO);

    }
}
