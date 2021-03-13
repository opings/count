package com.hupu.deep.comment.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author jiangfangyuan
 * @since 2020-03-12 13:32
 */
@Data
public class LightSubjectDO {

    private Long id;

    private String outBizNo;

    private String outBizType;

    private String subjectId;

    private Date createDt;

    private Date updateDt;


}
