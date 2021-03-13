package com.hupu.deep.comment.model;

import com.hupu.deep.comment.enums.LightStatusEnum;
import lombok.Data;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 00:05:00
 */
@Data
public class LightRecordModel {

    /**
     * 用户
     */
    private Long userId;

    /**
     * 点亮主体
     */
    private String subjectId;


    /**
     * 点亮状态
     */
    private LightStatusEnum lightStatusEnum;

}
