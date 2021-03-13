package com.hupu.deep.comment.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 点亮状态
 *
 * @author zhuwenkang
 * @Time 2020年03月11日 11:00:00
 */
public enum LightStatusEnum {
    /**
     * 点亮
     */
    LIGHT("LIGHT"),

    /**
     * 取消点亮
     */
    CANCEL_LIGHT("CANCEL_LIGHT");

    @Getter
    private String code;

    LightStatusEnum(String code) {
        this.code = code;
    }


    public static LightStatusEnum getByCode(String code) {
        return Arrays.stream(LightStatusEnum.values()).filter(item -> StringUtils.equalsIgnoreCase(item.getCode(), code))
                .findFirst().orElse(null);
    }


}
