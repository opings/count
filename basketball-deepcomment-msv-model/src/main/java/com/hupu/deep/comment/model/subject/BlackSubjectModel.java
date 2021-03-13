package com.hupu.deep.comment.model.subject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

/**
 * 点灭主体
 *
 * @author jiangfangyuan
 * @since 2020-03-11 12:25
 */
@Data
public class BlackSubjectModel extends SubjectModel {


    private static final long serialVersionUID = -2645170757154716712L;
    /**
     * 点灭数
     */
    private Integer blackCount;

    @JsonCreator
    public BlackSubjectModel(@JsonProperty("outBizKey") OutBizKey outBizKey, @JsonProperty("subjectId") String subjectId) {
        super(outBizKey, subjectId);
    }


}
