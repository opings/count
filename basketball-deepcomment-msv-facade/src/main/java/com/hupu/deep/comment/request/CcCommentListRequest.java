package com.hupu.deep.comment.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangfangyuan
 * @since 2020-03-22 16:28
 */
@Data
public class CcCommentListRequest extends BaseCommentRequest implements Serializable {

    private static final long serialVersionUID = -7756938014820550637L;


    /**
     * 游标
     */
    private CommentCursor commentCursor;

}
