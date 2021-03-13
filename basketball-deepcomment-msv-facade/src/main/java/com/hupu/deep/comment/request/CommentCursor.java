package com.hupu.deep.comment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangfangyuan
 * @since 2020-03-22 16:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCursor {

    /**
     * 评论发布时间
     */
    private Long publishTime;

    /**
     * 返回条数
     */
    private Integer limit;


}
