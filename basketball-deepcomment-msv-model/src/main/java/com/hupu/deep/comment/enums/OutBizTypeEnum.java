package com.hupu.deep.comment.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author jiangfangyuan
 * @since 2020-03-18 02:23
 */
public enum OutBizTypeEnum {

    /**
     * 评论
     */
    COMMENT("COMMENT"),

    /**
     * 打分
     */
    SCORE("SCORE"),

    /**
     * 点亮
     */
    LIGHT("LIGHT");

    @Getter
    private String code;

    OutBizTypeEnum(String code) {
        this.code = code;
    }


    public static OutBizTypeEnum getByCode(String code) {
        return Arrays.stream(OutBizTypeEnum.values()).filter(item -> StringUtils.equalsIgnoreCase(item.getCode(), code))
                .findFirst().orElse(null);
    }

    public static boolean isValid(String code) {
        return getByCode(code) != null;
    }
}
