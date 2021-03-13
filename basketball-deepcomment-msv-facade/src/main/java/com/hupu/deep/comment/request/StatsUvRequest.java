package com.hupu.deep.comment.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiangfangyuan
 * @since 2020-05-27 00:05
 */
@Data
public class StatsUvRequest extends BaseCommentRequest implements Serializable {

    private static final long serialVersionUID = 1933741367536427576L;

    private List<String> contentList;

    private Long beginTime;

    private Long endTime;
}
