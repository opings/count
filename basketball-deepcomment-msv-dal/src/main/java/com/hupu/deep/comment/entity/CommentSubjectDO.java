package com.hupu.deep.comment.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CommentSubjectDO {

    private Long id;

    private String subjectId;

    private String outBizType;

    private String outBizNo;

    private Integer commentCount;

    private Integer lightCount;

    private Date createDt;

    private Date updateDt;
}