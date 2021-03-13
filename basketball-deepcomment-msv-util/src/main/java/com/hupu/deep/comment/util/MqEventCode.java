package com.hupu.deep.comment.util;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 14:48
 */
public class MqEventCode {


    /**
     * 发布评论
     */
    public static final String EC_PUBLISH_COMMENT = "EC_PUBLISH_COMMENT";
    /**
     * 审核评论
     */
    public static final String EC_AUDIT_COMMENT = "EC_AUDIT_COMMENT";


    /**
     * 点亮
     */
    public static final String EC_LIGHT = "EC_light";
    /**
     * 取消点亮
     */
    public static final String EC_CANCEL_LIGHT = "EC_cancelLight";

    /**
     * 点灭
     */
    public static final String EC_BLACK = "EC_black";
    /**
     * 取消点灭
     */
    public static final String EC_CANCEL_BLACK = "EC_cancelBlack";


    /**
     * 打分
     */
    public static final String EC_SCORE = "EC_score";

    /**
     * 合并送礼弹幕
     */
    public static final String EC_MERGE_GIFT_DANMAKU = "sink_mq_gift_to_comment";





}
