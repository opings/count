package com.hupu.deep.comment.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import com.hupu.foundation.util.AssertUtil;
import lombok.Data;
import lombok.Getter;

/**
 * @author jiangfangyuan
 * @since 2020-03-20 12:36
 */
@Data
public class ScoreKey {


    /**
     * 主体id
     */
    @Getter
    private String subjectId;

    /**
     * 评分Id
     */
    @Getter
    private String scoreId;

    @JsonCreator
    public ScoreKey(@JsonProperty("subjectId") String subjectId, @JsonProperty("scoreId") String scoreId) {

        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notBlank(scoreId, () -> "commentId empty");

        this.subjectId = subjectId;
        this.scoreId = scoreId;
    }

    public String getKey() {

        return Joiner.on(":").join(subjectId, scoreId);
    }

}
