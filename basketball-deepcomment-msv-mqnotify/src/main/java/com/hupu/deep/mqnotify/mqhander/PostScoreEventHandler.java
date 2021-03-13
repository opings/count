package com.hupu.deep.mqnotify.mqhander;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.hupu.deep.comment.dal.ScoreRecordMapper;
import com.hupu.deep.comment.dal.invertIndex.UserScoreRecordMapper;
import com.hupu.deep.comment.entity.ScoreRecordDO;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.count.service.CountService;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.deep.score.helper.ScoreHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 评分后置处理
 *
 * @author yuepenglei
 * @since 2020-03-16 15:22
 */
@Service
@Slf4j
public class PostScoreEventHandler implements EventHandler {

    @Autowired
    private ScoreRecordMapper scoreRecordMapper;

    @Autowired
    private UserScoreRecordMapper userScoreRecordMapper;

    @Autowired
    private CountService countService;


    @Autowired
    private ScoreHelper scoreHelper;

    @Autowired
    private IdService idService;


    @Override
    public Action handleMsg(Message message) {

        Map<String, Object> msgBodyMap = JSON.parseObject(new String(message.getBody()),
                new TypeReference<Map<String, Object>>() {
                });
        Long userId = Long.valueOf(msgBodyMap.get("userId").toString());
        String subjectId = msgBodyMap.get("subjectId").toString();

        log.info("开始维护评分 反向索引表 userId:{} subjectId:{}", userId, subjectId);

        ScoreRecordDO userScoreRecordDO = userScoreRecordMapper.getScoreRecord(userId, subjectId);
        //AssertUtil.notNull(userScoreRecordDO, () -> "评分记录不存在 userId " + userId + " subjectId" + subjectId);
        if(userScoreRecordDO == null){
            return Action.CommitMessage;
        }


        //计数
        countService.publishOrUpdateCountFlow(scoreHelper.buildTotalPersonCounterKey(subjectId), Globals.NUM_1, userScoreRecordDO.getScoreRecordId());
        countService.publishOrUpdateCountFlow(scoreHelper.buildTotalScoreCounterKey(subjectId), userScoreRecordDO.getScore(), userScoreRecordDO.getScoreRecordId());

        //保存评分记录
        if (null == scoreRecordMapper.getScoreRecord(userId, subjectId)) {
            try {
                // 新增评分记录
                ScoreRecordDO scoreRecordDO = new ScoreRecordDO();
                scoreRecordDO.setUserId(userScoreRecordDO.getUserId());
                scoreRecordDO.setSubjectId(userScoreRecordDO.getSubjectId());
                scoreRecordDO.setScoreRecordId(userScoreRecordDO.getScoreRecordId());
                scoreRecordDO.setScore(userScoreRecordDO.getScore());
                scoreRecordMapper.saveScoreRecord(scoreRecordDO);
            } catch (DuplicateKeyException ex) {
                log.info("重复插入评分记录 userId {} subjectId {}", userId, subjectId);
                scoreRecordMapper.updateScoreRecord(userId, subjectId, userScoreRecordDO.getScore());
            }
            return Action.CommitMessage;
        }
        // 更新评分记录
        scoreRecordMapper.updateScoreRecord(userId, subjectId, userScoreRecordDO.getScore());

        return Action.CommitMessage;
    }

}
