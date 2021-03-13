package com.hupu.deep.comment.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author zhuwenkang
 * @Time 2020年03月13日 00:34:00
 */
public enum CommentSortedType {

    LIGHT_COUNTS("lightCount"),

    LATEST_DATE("latestDate");

    @Getter
    private String code;

    CommentSortedType(String code) {
        this.code = code;
    }

    public static CommentSortedType getByCode(String code) {
        return Arrays.stream(CommentSortedType.values()).filter(item -> StringUtils.equalsIgnoreCase(item.getCode(), code))
                .findFirst().orElse(null);
    }

    public static boolean isValid(String code) {
        return getByCode(code) != null;
    }
}
