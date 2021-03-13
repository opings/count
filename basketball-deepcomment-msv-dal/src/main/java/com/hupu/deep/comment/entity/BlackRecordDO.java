package com.hupu.deep.comment.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BlackRecordDO {

    private Long id;

    private String blackRecordId;

    private Long userId;

    private String subjectId;

    private String blackStatus;

    private Date createDt;

    private Date updateDt;

}