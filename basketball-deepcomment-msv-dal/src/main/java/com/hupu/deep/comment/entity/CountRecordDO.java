package com.hupu.deep.comment.entity;

import lombok.Data;

import java.util.Date;

/**
 * 计数流水
 */
@Data
public class CountRecordDO {

    private Long id;

    private String subjectId;

    private String countRecordId;

    private Integer countValue;

    /**
     * 幂等单据号
     */
    private String idempotentNo;

    private Date createDt;

    private Date updateDt;

}