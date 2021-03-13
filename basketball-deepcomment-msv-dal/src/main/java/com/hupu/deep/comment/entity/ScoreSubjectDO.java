package com.hupu.deep.comment.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ScoreSubjectDO {
    private Long id;

    private String subjectId;

    private String outBizNo;

    private String outBizType;

    private Date createDt;

    private Date updateDt;
}