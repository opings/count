package com.hupu.deep.comment.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author jiangfangyuan
 * @since 2020-03-12 13:32
 */
@Data
public class SplitCountSubjectDO {

    private Long id;

    private String subjectId;

    private Integer countValue;

    private String parentSubjectId;

    private Date createDt;

    private Date updateDt;


}
