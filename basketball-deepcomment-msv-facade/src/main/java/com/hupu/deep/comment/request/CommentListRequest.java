package com.hupu.deep.comment.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuepenglei
 * @Time 2020年03月13日 00:12:00
 */
@Data
public class CommentListRequest extends BaseCommentRequest implements Serializable {

    private static final long serialVersionUID = 7979572830573158561L;


    /**
     * 评论id列表
     */
    private List<String> commentIdList;


    /**
     * 游标
     */
    private CommentCursor commentCursor;


    /**
     * 评论状态列表
     * 只针对游标 列表
     */
    private List<String> commentStatusList;


}
