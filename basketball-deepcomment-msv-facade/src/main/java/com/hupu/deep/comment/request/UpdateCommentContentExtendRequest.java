package com.hupu.deep.comment.request;

import com.hupu.deep.comment.types.CommentKey;
import lombok.Data;

import java.util.List;

/**
 * @author lisongqiu
 */
@Data
public class UpdateCommentContentExtendRequest {

    private CommentKey commentKey;

    private String commentContentExtend;
}
