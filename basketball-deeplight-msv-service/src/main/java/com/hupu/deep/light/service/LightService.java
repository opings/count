package com.hupu.deep.light.service;

import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.google.common.collect.Maps;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.client.mq.MqClient;
import com.hupu.deep.comment.dal.invertIndex.UserLightRecordMapper;
import com.hupu.deep.comment.entity.LightRecordDO;
import com.hupu.deep.comment.enums.BizTypeEnum;
import com.hupu.deep.comment.enums.LightStatusEnum;
import com.hupu.deep.comment.model.LightRecordModel;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.comment.util.MqEventCode;
import com.hupu.deep.comment.util.error.ErrorCode;
import com.hupu.deep.count.service.CountService;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.deep.light.helper.LightHelper;
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
public class LightService {


    @Autowired
    private MqClient mqClient;

    @Autowired
    private UserLightRecordMapper userLightRecordMapper;

    @Autowired
    private LightHelper lightHelper;

    @Autowired
    private ConfigClient configClient;

    @Autowired
    private CountService countService;

    @Autowired
    private IdService idService;


    /**
     * 点亮
     *
     * @param userId
     * @param subjectId
     */
    public void light(Long userId, String subjectId) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        LightRecordDO userLightRecordDO = userLightRecordMapper.getLightRecord(userId, subjectId);
        if (Objects.nonNull(userLightRecordDO)) {
            if (StringUtils.equalsIgnoreCase(userLightRecordDO.getLightStatus(), LightStatusEnum.LIGHT.getCode())) {
                return;
            }
        }

        Map<String, Object> msgBody = Maps.newHashMap();
        msgBody.put("userId", userId);
        msgBody.put("subjectId", subjectId);

        mqClient.sendTransactionMsg(configClient.getLightTopic(), MqEventCode.EC_LIGHT, msgBody, (msg, arg) -> {
            try {
                if (null == userLightRecordDO) {
                    try {
                        LightRecordDO lightRecord = new LightRecordDO();
                        lightRecord.setUserId(userId);
                        lightRecord.setSubjectId(subjectId);
                        lightRecord.setLightRecordId(idService.genId(BizTypeEnum.LIGHT.getCode(),userId, Globals.DB_SIZE).toString());
                        lightRecord.setLightStatus(LightStatusEnum.LIGHT.getCode());
                        userLightRecordMapper.saveLightRecord(lightRecord);
                    } catch (DuplicateKeyException ex) {
                        log.warn("userId {} 对 {} 重复点亮", userId, subjectId);
                        userLightRecordMapper.light(userId, subjectId);
                    }
                    return TransactionStatus.CommitTransaction;
                }
                userLightRecordMapper.light(userId, subjectId);
                return TransactionStatus.CommitTransaction;
            } catch (Exception ex) {
                log.error("light error userId" + userId + " subjectId" + subjectId, ex);
                return TransactionStatus.RollbackTransaction;
            }
        }, msgBody);
    }


    /**
     * 取消点亮
     *
     * @param userId
     * @param subjectId
     */
    public void cancelLight(Long userId, String subjectId) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        LightRecordDO userLightRecordDO = userLightRecordMapper.getLightRecord(userId, subjectId);
        if (null == userLightRecordDO) {
            throw new CommonSystemException(ErrorCode.NOT_EXIST_LIGHT_SUBJECT.getCode(),
                    ErrorCode.NOT_EXIST_LIGHT_SUBJECT.getMsg()).putExtra("subjectId", subjectId)
                    .putExtra("userId", userId);
        }
        if (StringUtils.equalsIgnoreCase(userLightRecordDO.getLightStatus(), LightStatusEnum.CANCEL_LIGHT.getCode())) {
            return;
        }
        Map<String, Object> msgBody = Maps.newHashMap();
        msgBody.put("userId", userId);
        msgBody.put("subjectId", subjectId);

        mqClient.sendTransactionMsg(configClient.getLightTopic(), MqEventCode.EC_CANCEL_LIGHT, msgBody, (msg, arg) -> {
            try {
                userLightRecordMapper.cancelLight(userId, subjectId);
                return TransactionStatus.CommitTransaction;
            } catch (Exception ex) {
                log.error("cancelLightInner error userId" + userId + " subjectId" + subjectId, ex);
                return TransactionStatus.RollbackTransaction;
            }
        }, msgBody);
    }

    /**
     * 查询某个用户是否对某个主体进行点亮过
     *
     * @param userId    userId
     * @param subjectId subjectId
     * @return 是否点亮
     */
    public boolean hasLight(Long userId, String subjectId) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        LightRecordDO lightRecordDO = userLightRecordMapper.getLightRecord(userId, subjectId);
        return Objects.nonNull(lightRecordDO) &&
                StringUtils.equalsIgnoreCase(lightRecordDO.getLightStatus(), LightStatusEnum.LIGHT.getCode());
    }


    public LightRecordModel getUserLightRecordModel(Long userId, String subjectId) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        LightRecordDO lightRecordDO = userLightRecordMapper.getLightRecord(userId, subjectId);
        return lightHelper.buildLightRecordModel(lightRecordDO);

    }
}
