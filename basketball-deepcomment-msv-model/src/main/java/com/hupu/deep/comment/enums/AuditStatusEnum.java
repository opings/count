package com.hupu.deep.comment.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 审核状态
 *
 * @author zhuwenkang
 * @Time 2020年03月11日 13:32:00
 */
public enum AuditStatusEnum {

    /**
     * 待审核
     */
    WAITING_AUDIT("WAITING_AUDIT", "待审核"),

    /**
     * 审核通过
     */
    AUDIT_PASS("AUDIT_PASS", "审核通过"),

    /**
     * 审核不通过
     */
    AUDIT_NOT_PASS("AUDIT_NOT_PASS", "审核不通过"),

    /**
     * 删除状态
     */
    DELETE("DELETE", "删除");

    @Getter
    private String code;

    @Getter
    private String desc;


    AuditStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AuditStatusEnum getByCode(String code) {
        return Arrays.stream(AuditStatusEnum.values()).filter(item -> StringUtils.equalsIgnoreCase(item.getCode(), code))
                .findFirst().orElse(null);
    }

}
