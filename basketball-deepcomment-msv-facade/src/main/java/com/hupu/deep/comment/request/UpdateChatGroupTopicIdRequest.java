package com.hupu.deep.comment.request;

import com.hupu.deep.comment.types.CommentKey;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * @author lisongqiu
 */
@Data
public class UpdateChatGroupTopicIdRequest {

    private String subjectId;

    private List<String> rongcloudMsgIdList;

    private String chatGroupTopicId;
}
