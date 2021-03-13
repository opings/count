package com.hupu.deep.mqnotify.mqhander;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.hupu.deep.comment.dal.BlackRecordMapper;
import com.hupu.deep.comment.dal.invertIndex.UserBlackRecordMapper;
import com.hupu.deep.comment.entity.BlackRecordDO;
import com.hupu.deep.comment.enums.BlackStatusEnum;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.count.service.CountService;
import com.hupu.deep.black.helper.BlackHelper;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 点灭后置处理
 * 消息乱序 会导致计数错误.
 *
 * @author jiangfangyuan
 * @since 2020-03-11 15:22
 */
@Service
@Slf4j
public class PostBlackEventHandler implements EventHandler {


    @Autowired
    private BlackRecordMapper blackRecordMapper;

    @Autowired
    private UserBlackRecordMapper userBlackRecordMapper;

    @Autowired
    private CountService countService;

    @Autowired
    private BlackHelper blackHelper;


    @Override
    public Action handleMsg(Message message) {

        Map<String, Object> msgBodyMap = JSON.parseObject(new String(message.getBody()),
                new TypeReference<Map<String, Object>>() {
                });
        Long userId = Long.valueOf(msgBodyMap.get("userId").toString());
        String subjectId = msgBodyMap.get("subjectId").toString();

        log.info("开始进行点灭反向索引表同步 userId:{} subjectId:{}", userId, subjectId);

        BlackRecordDO userBlackRecordDO = userBlackRecordMapper.getBlackRecord(userId, subjectId);
        AssertUtil.notNull(userBlackRecordDO, () -> "点灭记录不存在 userId " + userId + " subjectId" + subjectId);

        if (null == blackRecordMapper.getBlackRecord(userId, subjectId)) {
            try {
                BlackRecordDO blackRecordDO = new BlackRecordDO();
                blackRecordDO.setUserId(userBlackRecordDO.getUserId());
                blackRecordDO.setSubjectId(userBlackRecordDO.getSubjectId());
                blackRecordDO.setBlackRecordId(userBlackRecordDO.getBlackRecordId());
                blackRecordDO.setBlackStatus(userBlackRecordDO.getBlackStatus());
                blackRecordMapper.saveBlackRecord(blackRecordDO);
                countService.publishOrUpdateCountFlow(blackHelper.buildTotalBlackCounterKey(userBlackRecordDO.getSubjectId()),
                        Globals.NUM_1, userBlackRecordDO.getBlackRecordId());
                return Action.CommitMessage;
            } catch (DuplicateKeyException ex) {
                log.info("重复插入点灭记录 userId {} subjectId {}", userId, subjectId);
            }
        }

        if (StringUtils.equalsIgnoreCase(userBlackRecordDO.getBlackStatus(), BlackStatusEnum.BLACK.getCode())) {
            blackRecordMapper.black(userId, subjectId);
            countService.publishOrUpdateCountFlow(blackHelper.buildTotalBlackCounterKey(userBlackRecordDO.getSubjectId()),
                    Globals.NUM_1, userBlackRecordDO.getBlackRecordId());
            return Action.CommitMessage;
        }
        if (StringUtils.equalsIgnoreCase(userBlackRecordDO.getBlackStatus(), BlackStatusEnum.CANCEL_BLACK.getCode())) {
            blackRecordMapper.cancelBlack(userId, subjectId);
            countService.publishOrUpdateCountFlow(blackHelper.buildTotalBlackCounterKey(userBlackRecordDO.getSubjectId()),
                    Globals.NUM_0, userBlackRecordDO.getBlackRecordId());
            return Action.CommitMessage;
        }
        throw new RuntimeException("can not arrive here");
    }

}
