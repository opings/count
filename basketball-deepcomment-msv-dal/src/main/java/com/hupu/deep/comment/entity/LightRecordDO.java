package com.hupu.deep.comment.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LightRecordDO {

    private Long id;

    private String lightRecordId;

    private Long userId;

    private String subjectId;

    private String lightStatus;

    private Date createDt;

    private Date updateDt;

}