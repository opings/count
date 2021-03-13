package com.hupu.deep.comment.client.dto;

import lombok.Data;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 17:40
 */
@Data
public class ProhibitionResult {

    /**
     * 服务是否调用成功
     */
    private boolean success;


    /**
     * 是否被封禁
     */
    private boolean isProhibition;


}
