package com.hupu.deep.comment.request;

import com.hupu.deep.OutBizAble;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author zhuwenkang
 * @Time 2020年03月16日 20:31:00
 */
@Data
public class QueryCommentSubjectRequest implements Serializable, OutBizAble {

    /**
     * 外部业务类型
     */
    @NotBlank
    private String outBizType;

    /**
     * 外部业务单据号
     */
    @NotBlank
    private String outBizNo;
}
