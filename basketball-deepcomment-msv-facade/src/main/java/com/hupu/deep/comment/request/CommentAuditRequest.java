package com.hupu.deep.comment.request;

import com.hupu.deep.comment.enums.AuditStatusEnum;
import lombok.Data;

/**
 * 评论审核请求
 *
 * @author jiangfangyuan
 * @since 2020-03-13 10:35
 */
@Data
public class CommentAuditRequest {


    /**
     * 主体id
     */
    private String subjectId;

    /**
     * 评论id
     */
    private String commentId;


    /**
     * 审核状态
     *
     * @see AuditStatusEnum
     */
    private String auditStatus;

    /**
     * 审核人
     */
    private String auditUserName;


}
