package com.hupu.deep.comment.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ScoreRecordDO {
    private Long id;

    private String subjectId;

    private String scoreRecordId;

    private Long userId;

    private Integer score;

    private Date createDate;

    private Date updateDate;
}