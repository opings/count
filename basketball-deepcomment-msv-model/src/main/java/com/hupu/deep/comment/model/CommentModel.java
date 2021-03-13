package com.hupu.deep.comment.model;

import com.hupu.deep.comment.enums.AuditStatusEnum;
import com.hupu.deep.comment.types.CommentKey;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * @author zhuwenkangU
 * @Time 2020年03月11日 00:01:00
 */
@Data
public class CommentModel implements Serializable {


    private static final long serialVersionUID = 8547785788352207529L;


    /**
     * 评论key
     */
    private CommentKey commentKey;

    /**
     * 用户
     */
    private Long userId;

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
     * 机器审核状态
     */
    private AuditStatusEnum machineAuditStatus;


    /**
     * 人工审核状态
     */
    private AuditStatusEnum manualAuditStatus;


    /**
     * 最终审核状态
     */
    private AuditStatusEnum finalAuditStatus;


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
     * 发布毫秒数
     */
    private Long publishTime;

    /**
     * 父评论
     */
    private CommentModel parentCommentModel;

    private String commentContentExtend;

    /**
     * 审核人用户Id
     */
    private String auditUserName;


    public boolean canSee(Long userId) {

        if (Objects.equals(getFinalAuditStatus(), AuditStatusEnum.AUDIT_PASS)) {
            return true;
        }
        if (Objects.isNull(userId)) {
            return false;
        }

        if (Objects.equals(getFinalAuditStatus(), AuditStatusEnum.AUDIT_NOT_PASS)) {
            return Objects.equals(userId, getUserId());
        }

        if (Objects.equals(getFinalAuditStatus(), AuditStatusEnum.WAITING_AUDIT)) {
            return Objects.equals(userId, getUserId());
        }

        if (Objects.equals(getFinalAuditStatus(), AuditStatusEnum.DELETE)) {
            return false;
        }
        return false;
    }

    
    public boolean canSeeByLightsAndBlack() {
        return Optional.ofNullable(blackCount).orElse(0)
                - Optional.ofNullable(lightCount).orElse(0) < 10;
    }

    public boolean canShowHotComment() {
        return Optional.ofNullable(lightCount).orElse(0)
                >= Optional.ofNullable(blackCount).orElse(0);
    }

}
