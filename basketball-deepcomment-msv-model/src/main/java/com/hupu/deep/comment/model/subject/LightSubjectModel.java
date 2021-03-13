package com.hupu.deep.comment.model.subject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

/**
 * 点亮主体
 *
 * @author jiangfangyuan
 * @since 2020-03-11 12:25
 */
@Data
public class LightSubjectModel extends SubjectModel {


    private static final long serialVersionUID = 5702547038666618098L;
    /**
     * 点亮数
     */
    private Integer lightCount;

    @JsonCreator
    public LightSubjectModel(@JsonProperty("outBizKey") OutBizKey outBizKey, @JsonProperty("subjectId") String subjectId) {
        super(outBizKey, subjectId);
    }


}
