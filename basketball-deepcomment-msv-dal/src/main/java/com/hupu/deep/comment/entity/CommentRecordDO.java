package com.hupu.deep.comment.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommentRecordDO implements Serializable {


    private Long id;


    /**
     * 用户
     */
    private Long userId;


    /**
     * 主体id
     */
    private String subjectId;


    /**
     * 评论Id
     */
    private String commentId;

    /**
     * 父评论id
     */
    private String parentCommentId;

    /**
     * 融云消息id
     */
    private String rongcloudMsgId;

    /**
     * 融云消息时间
     */
    private Long rongcloudMsgTime;

    /**
     * 融云聊天室消息id
     */
    private String rongcloudRoomMsgId;

    /**
     * 融云聊天室消息时间
     */
    private Long rongcloudRoomMsgTime;

    /**
     * 团话题id
     */
    private String chatGroupTopicId;

    /**
     * 评论内容 长度限制1000
     */
    private String commentContent;

    /**
     * 评论扩展内容 长度限制512
     */
    private String commentContentExtend;

    /**
     * 机器审核状态
     */
    private String machineAuditStatus;

    /**
     * 人工审核状态
     */
    private String manualAuditStatus;


    /**
     * 最终审核状态
     */
    private String finalAuditStatus;


    /**
     * 回复数
     */
    private Integer replyCount;


    /**
     * 点亮数
     */
    private Integer lightCount;

    /**
     * 点灭数
     */
    private Integer blackCount;

    /**
     * 机器审核请求唯一标识
     */
    private String machineAuditRequestId;

    private Date createDt;

    private Date updateDt;


    private Long publishTime;

    private String auditUserName;

}