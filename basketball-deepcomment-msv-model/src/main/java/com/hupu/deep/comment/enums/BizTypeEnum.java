package com.hupu.deep.comment.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author jiangfangyuan
 * @since 2020-03-18 02:23
 */
public enum BizTypeEnum {

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
    LIGHT("LIGHT"),
    /**
     * 点灭
     */
    BLACK("BLACK");

    @Getter
    private String code;

    BizTypeEnum(String code) {
        this.code = code;
    }


    public static BizTypeEnum getByCode(String code) {
        return Arrays.stream(BizTypeEnum.values()).filter(item -> StringUtils.equalsIgnoreCase(item.getCode(), code))
                .findFirst().orElse(null);
    }

    public static boolean isValid(String code) {
        return getByCode(code) != null;
    }
}
