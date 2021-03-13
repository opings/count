package com.hupu.deep.comment.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 2 * @Author: xuronghua
 * 3 * @Date: 2020/5/21 7:56 PM
 * 4
 */
@Data
public class PassedCommentPageListRequest extends BaseCommentRequest implements Serializable {
    private static final long serialVersionUID = -2912862495574885881L;
    private Integer startIndex;
    private Integer limit;
    private String version = "0.0.1";
}
