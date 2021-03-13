package com.hupu.deep.comment.model.subject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 00:00:00
 */
@Data
public class SubjectModel implements Serializable {


    private static final long serialVersionUID = -7652009211207486364L;

    /**
     * 外部业务关联
     */
    private OutBizKey outBizKey;

    /**
     * 主体Id
     */
    private String subjectId;


    public SubjectModel() {

    }

    @JsonCreator
    public SubjectModel(@JsonProperty("outBizKey") OutBizKey outBizKey, @JsonProperty("subjectId") String subjectId) {
        this.outBizKey = outBizKey;
        this.subjectId = subjectId;
    }


    public String getOutBizType() {
        return outBizKey.getOutBizType();
    }
}
