package com.hupu.deep.comment.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuepenglei
 * @Time 2020年03月13日 00:12:00
 */
@Data
public class CommentPageListRequest extends BaseCommentRequest implements Serializable {


    private static final long serialVersionUID = -2912862495574885881L;
    private Integer startIndex;

    private Integer limit;
}
