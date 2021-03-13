package com.hupu.deep.comment.model;

import com.hupu.deep.comment.enums.BlackStatusEnum;
import lombok.Data;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 00:05:00
 */
@Data
public class BlackRecordModel {

    /**
     * 用户
     */
    private Long userId;

    /**
     * 点灭主体
     */
    private String subjectId;


    /**
     * 点灭状态
     */
    private BlackStatusEnum blackStatusEnum;

}
