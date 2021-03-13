package com.hupu.deep.score.service;

import com.google.common.collect.Lists;
import com.hupu.deep.comment.dal.ScoreSubjectMapper;
import com.hupu.deep.comment.dal.invertIndex.UserScoreRecordMapper;
import com.hupu.deep.comment.entity.ScoreRecordDO;
import com.hupu.deep.comment.entity.ScoreSubjectDO;
import com.hupu.deep.comment.enums.BizTypeEnum;
import com.hupu.deep.comment.model.ScoreRecordModel;
import com.hupu.deep.comment.model.subject.ScoreSubjectModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.CacheConfig;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.deep.score.helper.ScoreHelper;
import com.hupu.foundation.cache.CacheClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author yuepenglei
 * @Description 打分主体API
 * @Date 2020/03/16 09:55
 */

@Service
@Slf4j
public class ScoreSubjectService {

    @Autowired
    private ScoreSubjectMapper scoreSubjectMapper;

    @Autowired
    private UserScoreRecordMapper userScoreRecordMapper;

    @Autowired
    private ScoreHelper scoreHelper;


    @Autowired
    private CacheClient cacheClient;


    @Autowired
    private IdService idService;


    /**
     * 获取主体ID
     *
     * @param outBizKey 外部业务实体
     * @return
     */
    public String getOrCreateSubjectId(OutBizKey outBizKey) {
        String subjectId = getSubjectId(outBizKey);
        if (StringUtils.isBlank(subjectId)) {
            return createSubject(outBizKey);
        }
        return subjectId;
    }

    /**
     * 获取主体ID
     *
     * @param outBizKey 外部业务实体
     * @return
     */
    public String getSubjectId(OutBizKey outBizKey) {
        return cacheClient.get(CacheConfig.SCORE, outBizKey.getKey(), () -> {
            ScoreSubjectDO scoreSubjectDO = scoreSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo());
            return Optional.ofNullable(scoreSubjectDO).map(ScoreSubjectDO::getSubjectId).orElse(null);
        }, Globals.NUM_60);
    }

        /**
         * 创建打分主体
         *
         * @param outBizKey 外部业务实体
         * @return
         */
    public String createSubject(OutBizKey outBizKey) {
        try {
            String subjectId = idService.genId(BizTypeEnum.SCORE.getCode()).toString();
            ScoreSubjectDO scoreSubjectDO = new ScoreSubjectDO();
            scoreSubjectDO.setSubjectId(subjectId);
            scoreSubjectDO.setOutBizType(outBizKey.getOutBizType());
            scoreSubjectDO.setOutBizNo(outBizKey.getOutBizNo());
            scoreSubjectMapper.insertSelective(scoreSubjectDO);
            return subjectId;
        } catch (DuplicateKeyException ex) {
            log.info("重复创建打分主体 " + outBizKey);
            return scoreSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo()).getSubjectId();
        }
    }

    /**
     * 获取主体打分汇总信息
     *
     * @param outBizKey 外部业务类型
     * @return
     */
    public ScoreSubjectModel getScoreSubject(OutBizKey outBizKey) {
        return scoreHelper.buildScoreSubjectModel(outBizKey, getSubjectId(outBizKey));
    }


    /**
     * 根据外部单号获取主体打分记录
     * @param userId
     * @param outBizKey
     * @return
     */
    public ScoreRecordModel getScoreRecord(Long userId, OutBizKey outBizKey) {
        ScoreRecordDO scoreRecordDO = userScoreRecordMapper.getScoreRecord(userId, getSubjectId(outBizKey));
        Map<String,OutBizKey> subjectId2OutBizKeyMap = new HashMap<>();
        if(scoreRecordDO != null) {
            subjectId2OutBizKeyMap.put(scoreRecordDO.getSubjectId(), outBizKey);
        }
        return scoreHelper.buildScoreRecordModel(subjectId2OutBizKeyMap,scoreRecordDO);
    }

    public List<ScoreRecordModel> getSingleUserScoreRecordList(Long userId, List<OutBizKey> outBizKeyList) {
        if(userId == null || CollectionUtils.isEmpty(outBizKeyList)){
            return Lists.newArrayList();
        }
        List<String> subjectIdList = outBizKeyList.stream().map(outBizKey -> getSubjectId(outBizKey)).collect(Collectors.toList());
        List<ScoreRecordDO> userScoreRecordDOList = userScoreRecordMapper.getSingleUserScoreRecordList(userId, subjectIdList);
        List<ScoreSubjectDO> scoreSubjectDOList = scoreSubjectMapper.selectBySubjectIds(subjectIdList);
        Map<String, OutBizKey> subjectId2OutBizKeyMap = scoreSubjectDOList.stream().map(scoreSubjectDO -> new ScoreSubjectModel(new OutBizKey(scoreSubjectDO.getOutBizType(), scoreSubjectDO.getOutBizNo()), scoreSubjectDO.getSubjectId()))
                .collect(Collectors.toMap(ScoreSubjectModel::getSubjectId, ScoreSubjectModel::getOutBizKey, (key1, key2) -> key2));
        return userScoreRecordDOList.stream().map(scoreRecordDO -> scoreHelper.buildScoreRecordModel(subjectId2OutBizKeyMap,scoreRecordDO)).collect(Collectors.toList());
    }

    public List<ScoreRecordModel> getMultipleUserScoreRecordList(List<Long> userIds, OutBizKey outBizKey) {
        if(outBizKey == null || CollectionUtils.isEmpty(userIds)){
            return Lists.newArrayList();
        }
        List<String> subjectIdList =Collections.singletonList(getSubjectId(outBizKey));
        List<ScoreRecordDO> userScoreRecordDOList = userScoreRecordMapper.getMultipleUserScoreRecordList(userIds, getSubjectId(outBizKey));
        List<ScoreSubjectDO> scoreSubjectDOList = scoreSubjectMapper.selectBySubjectIds(subjectIdList);
        Map<String, OutBizKey> subjectId2OutBizKeyMap = scoreSubjectDOList.stream().map(scoreSubjectDO -> new ScoreSubjectModel(new OutBizKey(scoreSubjectDO.getOutBizType(), scoreSubjectDO.getOutBizNo()), scoreSubjectDO.getSubjectId()))
                .collect(Collectors.toMap(ScoreSubjectModel::getSubjectId, ScoreSubjectModel::getOutBizKey, (key1, key2) -> key2));
        return userScoreRecordDOList.stream().map(scoreRecordDO -> scoreHelper.buildScoreRecordModel(subjectId2OutBizKeyMap, scoreRecordDO)).collect(Collectors.toList());
    }
}
