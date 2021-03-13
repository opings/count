package com.hupu.deep.score.helper;

import com.hupu.deep.comment.entity.ScoreRecordDO;
import com.hupu.deep.comment.model.ScoreRecordModel;
import com.hupu.deep.comment.model.subject.ScoreSubjectModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.count.service.CountService;
import com.hupu.foundation.util.AssertUtil;
import com.hupu.foundation.util.BigDecimalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author jiangfangyuan
 * @since 2020-03-17 23:40
 */
@Service
public class ScoreHelper {


    @Autowired
    private CountService countService;


    /**
     * 构造总评分 分数 计数key
     *
     * @param subjectId
     * @return
     */
    public OutBizKey buildTotalScoreCounterKey(String subjectId) {

        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        return new OutBizKey("score_total_score_counter", subjectId);
    }


    /**
     * 构造总评分 分数 计数key
     *
     * @param subjectId
     * @return
     */
    public OutBizKey buildTotalPersonCounterKey(String subjectId) {

        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        return new OutBizKey("score_total_person_counter", subjectId);
    }


    public ScoreSubjectModel buildScoreSubjectModel(OutBizKey outBizKey, String subjectId) {

        if (null == outBizKey || StringUtils.isBlank(subjectId)) {
            return null;
        }

        Integer totalScore = countService.queryCountValue(buildTotalScoreCounterKey(subjectId));
        Integer totalPerson = countService.queryCountValue(buildTotalPersonCounterKey(subjectId));

        ScoreSubjectModel scoreSubjectModel = new ScoreSubjectModel(outBizKey, subjectId);
        scoreSubjectModel.setTotalScore(totalScore);
        scoreSubjectModel.setScorePersonCount(totalPerson);

        if (totalScore != null && totalPerson != null && totalScore > Globals.NUM_0 && totalPerson > Globals.NUM_0) {
           BigDecimal scoreAvg = BigDecimalUtil.dividePossibleZero(totalScore, totalPerson);
           if(scoreAvg.compareTo(BigDecimal.TEN) == Globals.NUM_1)   {
               scoreAvg = BigDecimal.TEN;
           }
           scoreSubjectModel.setScoreAvg(scoreAvg);
        }else{
            scoreSubjectModel.setScoreAvg(BigDecimal.ZERO);
        }
        return scoreSubjectModel;
    }

    /**
     * 构建用户打分记录model
     *
     * @param scoreRecordDO
     * @return
     */
    public ScoreRecordModel buildScoreRecordModel(Map<String,OutBizKey> subjectId2OutBizKeyMap,ScoreRecordDO scoreRecordDO) {

        if (null == scoreRecordDO) {
            return null;
        }
        ScoreRecordModel scoreRecordModel = new ScoreRecordModel();
        scoreRecordModel.setScore(scoreRecordDO.getScore());
        scoreRecordModel.setUserId(scoreRecordDO.getUserId());
        scoreRecordModel.setScoreSubjectModel(new ScoreSubjectModel(subjectId2OutBizKeyMap.get(scoreRecordDO.getSubjectId()),scoreRecordDO.getSubjectId()));
        return scoreRecordModel;
    }

}
