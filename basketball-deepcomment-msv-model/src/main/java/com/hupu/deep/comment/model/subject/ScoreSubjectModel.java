package com.hupu.deep.comment.model.subject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 00:06:00
 */
@Data
public class ScoreSubjectModel extends SubjectModel {

    /**
     * 总打分人数
     */
    private Integer scorePersonCount;


    /**
     * 总打分
     */
    private Integer totalScore;

    /**
     * 平均分值
     */
    private BigDecimal scoreAvg;

    @JsonCreator
    public ScoreSubjectModel(@JsonProperty("outBizKey") OutBizKey outBizKey, @JsonProperty("subjectId") String subjectId) {
        super(outBizKey, subjectId);
    }
}
