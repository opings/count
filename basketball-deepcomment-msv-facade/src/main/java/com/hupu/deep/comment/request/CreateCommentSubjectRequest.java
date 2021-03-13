package com.hupu.deep.comment.request;

import com.hupu.deep.OutBizAble;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 14:04:00
 */
@Data
@ApiModel("创建主体请求")
public class CreateCommentSubjectRequest implements Serializable, OutBizAble {

    private static final long serialVersionUID = -2159147205240771009L;

    /**
     * 外部业务单号
     */
    @ApiModelProperty("外部业务单号")
    private String outBizNo;

    /**
     * 外部业务类型
     */
    @ApiModelProperty("外部业务类型")
    private String outBizType;
}
