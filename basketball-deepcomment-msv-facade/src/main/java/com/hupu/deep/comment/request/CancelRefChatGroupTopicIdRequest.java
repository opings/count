package com.hupu.deep.comment.request;

import com.hupu.deep.comment.types.CommentKey;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lisongqiu
 */
@Data
public class CancelRefChatGroupTopicIdRequest implements Serializable {

    private static final long serialVersionUID = 8420109983019415385L;

    private String subjectId;

    private String rongcloudMsgId;
}
