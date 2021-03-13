package com.hupu.deep.comment.model;

import com.hupu.deep.comment.model.subject.ScoreSubjectModel;
import lombok.Data;

import java.util.Date;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 00:08:00
 */
@Data
public class ScoreRecordModel {

    /**
     * 用户
     */
    private Long  userId;

    /**
     * 打分主体model
     */
    private ScoreSubjectModel scoreSubjectModel;

    /**
     * 分值
     */
    private Integer score;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;
}
