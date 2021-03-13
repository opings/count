package com.hupu.deep.comment.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 2 * @Author: xuronghua
 * 3 * @Date: 2020/4/13 1:25 PM
 * 4
 */
@Data
@ApiModel("发布视频直播礼物弹幕请求")
public class PublishGiftDanmakuRequest extends BaseCommentRequest implements Serializable {
    private static final long serialVersionUID = -5741627280769354906L;

    @ApiModelProperty("送礼记录Id")
    private Long rewardRecoderId;

    @ApiModelProperty("礼物名称")
    private String giftName;

    @ApiModelProperty("礼物数量")
    @Length(max = 1000)
    private Integer giftNum;

}
