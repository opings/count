package com.hupu.deep.comment.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 审核策略
 *
 * @author jiangfangyuan
 * @since 2020-03-18 02:23
 */
public enum AuditStrategy {

    /**
     * 先审后发
     */
    AUDIT_FIRST_PUBLISH("AUDIT_FIRST_PUBLISH"),

    /**
     * 先发后审
     */
    PUBLISH_FIRST_AUDIT("PUBLISH_FIRST_AUDIT"),

    ;

    @Getter
    private String code;

    AuditStrategy(String code) {
        this.code = code;
    }

    public static AuditStrategy getByCode(String code) {
        return Arrays.stream(AuditStrategy.values()).filter(item -> StringUtils.equalsIgnoreCase(item.getCode(), code))
                .findFirst().orElse(null);
    }

}
