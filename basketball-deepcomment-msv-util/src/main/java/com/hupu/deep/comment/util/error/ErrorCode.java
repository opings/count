package com.hupu.deep.comment.util.error;

import lombok.Getter;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 13:38
 */
public enum ErrorCode {


    /**
     * 用户未登录
     */
    USER_NOT_LOGIN("USER_NOT_LOGIN", "用户未登录"),


    /**
     * 不存在的点亮主体
     */
    NOT_EXIST_LIGHT_SUBJECT("NOT_EXIST_LIGHT_SUBJECT", "不存在的点亮主体"),

    /**
     * 不存在的点灭主体
     */
    NOT_EXIST_BLACK_SUBJECT("NOT_EXIST_BLACK_SUBJECT", "不存在的点灭主体"),

    /**
     * 不存在的回复主体
     */
    NOT_EXIST_COMMENT_SUBJECT("NOT_EXIST_COMMENT_SUBJECT", "不存在的回复主体"),

    /**
     * 不存在该评论
     */
    NOT_EXIST_COMMENT("NOT_EXIST_COMMENT", "不存在该评论"),


    /**
     * 此评论不可见
     */
    CAN_NOT_PUBLISH_THE_COMMENT("CAN_NOT_PUBLISH_THE_COMMENT", "不能发布评论"),

    /**
     * 不存在的计数流水
     */
    NOT_EXIST_COUNT_RECORD("NOT_EXIST_COUNT_RECORD", "不存在的计数流水"),


    /**
     * 用户被封禁了
     */
    USER_BE_PROHIBITION("USER_BE_PROHIBITION", "用户被封禁了"),


    /**
     * 未知异常
     */
    SYSTEM_ERROR("SYSTEM_ERROR", "人气大爆发,请稍后再试"),


    /**
     * 封禁服务不可用
     */
    PROHIBITION_SERVICE_UN_AVAILABLE("PROHIBITION_SERVICE_UN_AVAILABLE", "封禁服务不可用"),


    ;


    @Getter
    private String code;

    @Getter
    private String msg;


    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
