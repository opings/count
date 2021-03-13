package com.hupu.deep.comment.client.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 2 * @Author: xuronghua
 * 3 * @Date: 2020/4/8 5:35 PM
 * 4
 */

@Data
@Builder
public class PushMsgDTO<T> implements Serializable {

    private static final long serialVersionUID = 8547785788352208629L;

    private String id;

    private String tag;

    private Boolean isIncrement;

    private Long snapshotVersion;

    private T snapshotJson;

    private T incrementJson;

    private String afterVer;

    private String beforeVer;
}

