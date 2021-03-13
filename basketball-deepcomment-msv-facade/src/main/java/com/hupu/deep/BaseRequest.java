package com.hupu.deep;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhuwenkang
 * @Time 2020年03月13日 17:33:00
 */
@Data
public class BaseRequest implements Serializable {


    @NotNull
    private Long clientId;

    private static final long serialVersionUID = -4237637473953765277L;
    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("来源")
    private String source;
}
