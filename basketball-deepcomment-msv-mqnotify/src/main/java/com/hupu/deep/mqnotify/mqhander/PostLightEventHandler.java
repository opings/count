package com.hupu.deep.mqnotify.mqhander;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.hupu.deep.comment.dal.LightRecordMapper;
import com.hupu.deep.comment.dal.invertIndex.UserLightRecordMapper;
import com.hupu.deep.comment.entity.LightRecordDO;
import com.hupu.deep.comment.enums.LightStatusEnum;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.count.service.CountService;
import com.hupu.deep.light.helper.LightHelper;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 点亮后置处理
 * 消息乱序 会导致计数错误.
 *
 * @author jiangfangyuan
 * @since 2020-03-11 15:22
 */
@Service
@Slf4j
public class PostLightEventHandler implements EventHandler {


    @Autowired
    private LightRecordMapper lightRecordMapper;

    @Autowired
    private UserLightRecordMapper userLightRecordMapper;

    @Autowired
    private CountService countService;

    @Autowired
    private LightHelper lightHelper;


    @Override
    public Action handleMsg(Message message) {

        Map<String, Object> msgBodyMap = JSON.parseObject(new String(message.getBody()),
                new TypeReference<Map<String, Object>>() {
                });
        Long userId = Long.valueOf(msgBodyMap.get("userId").toString());
        String subjectId = msgBodyMap.get("subjectId").toString();

        log.info("开始进行点亮反向索引表同步 userId:{} subjectId:{}", userId, subjectId);

        LightRecordDO userLightRecordDO = userLightRecordMapper.getLightRecord(userId, subjectId);
        AssertUtil.notNull(userLightRecordDO, () -> "点亮记录不存在 userId " + userId + " subjectId" + subjectId);

        if (null == lightRecordMapper.getLightRecord(userId, subjectId)) {
            try {
                LightRecordDO lightRecordDO = new LightRecordDO();
                lightRecordDO.setUserId(userLightRecordDO.getUserId());
                lightRecordDO.setSubjectId(userLightRecordDO.getSubjectId());
                lightRecordDO.setLightRecordId(userLightRecordDO.getLightRecordId());
                lightRecordDO.setLightStatus(userLightRecordDO.getLightStatus());
                lightRecordMapper.saveLightRecord(lightRecordDO);
                countService.publishOrUpdateCountFlow(lightHelper.buildTotalLightCounterKey(userLightRecordDO.getSubjectId()),
                        Globals.NUM_1, userLightRecordDO.getLightRecordId());
                return Action.CommitMessage;
            } catch (DuplicateKeyException ex) {
                log.info("重复插入点亮记录 userId {} subjectId {}", userId, subjectId);
            }
        }

        if (StringUtils.equalsIgnoreCase(userLightRecordDO.getLightStatus(), LightStatusEnum.LIGHT.getCode())) {
            lightRecordMapper.light(userId, subjectId);
            countService.publishOrUpdateCountFlow(lightHelper.buildTotalLightCounterKey(userLightRecordDO.getSubjectId()),
                    Globals.NUM_1, userLightRecordDO.getLightRecordId());
            return Action.CommitMessage;
        }
        if (StringUtils.equalsIgnoreCase(userLightRecordDO.getLightStatus(), LightStatusEnum.CANCEL_LIGHT.getCode())) {
            lightRecordMapper.cancelLight(userId, subjectId);
            countService.publishOrUpdateCountFlow(lightHelper.buildTotalLightCounterKey(userLightRecordDO.getSubjectId()),
                    Globals.NUM_0, userLightRecordDO.getLightRecordId());
            return Action.CommitMessage;
        }
        throw new RuntimeException("can not arrive here");
    }

}
