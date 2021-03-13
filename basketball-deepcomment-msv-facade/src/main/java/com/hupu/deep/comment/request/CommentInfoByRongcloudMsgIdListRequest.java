package com.hupu.deep.comment.request;

import com.hupu.deep.BaseRequest;
import lombok.Data;

import java.util.List;

/**
 * @author lisongqiu
 */
@Data
public class CommentInfoByRongcloudMsgIdListRequest extends BaseRequest {

    private String subjectId;

    private List<String> rongcloudMsgIdList;
}
