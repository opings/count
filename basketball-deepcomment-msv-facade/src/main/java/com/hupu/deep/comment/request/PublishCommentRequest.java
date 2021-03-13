package com.hupu.deep.comment.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 22:14:00
 */
@Data
@ApiModel("发布评论请求")
public class PublishCommentRequest extends BaseCommentRequest implements Serializable {

    private static final long serialVersionUID = -5741627280769354906L;


    @ApiModelProperty("双亲回复Id")
    private String parentCommentId;

    @ApiModelProperty("回复内容 限制1000字")
    @Length(max = 1000)
    private String content;


    @ApiModelProperty("回复内容扩展属性")
    @Length(max = 512)
    private String contentExtend;

    @ApiModelProperty("团话题id")
    private String chatGroupTopicId;

    @ApiModelProperty("融云消息id")
    private String rongcloudMsgId;

    @ApiModelProperty("融云消息时间")
    private Long rongcloudMsgTime;
}
