package com.hupu.deep.comment.model.subject;

import lombok.Data;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 00:07:00
 */
@Data
public class CommentSubjectModel extends SubjectModel {

    /**
     * 回复数
     */
    private Integer commentCount;
}
