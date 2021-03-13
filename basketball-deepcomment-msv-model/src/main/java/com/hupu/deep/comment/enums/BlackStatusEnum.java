package com.hupu.deep.comment.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 点灭状态
 *
 * @author zhuwenkang
 * @Time 2020年03月11日 11:00:00
 */
public enum BlackStatusEnum {
    /**
     * 点灭
     */
    BLACK("BLACK"),

    /**
     * 取消点灭
     */
    CANCEL_BLACK("CANCEL_BLACK");

    @Getter
    private String code;

    BlackStatusEnum(String code) {
        this.code = code;
    }


    public static BlackStatusEnum getByCode(String code) {
        return Arrays.stream(BlackStatusEnum.values()).filter(item -> StringUtils.equalsIgnoreCase(item.getCode(), code))
                .findFirst().orElse(null);
    }


}
