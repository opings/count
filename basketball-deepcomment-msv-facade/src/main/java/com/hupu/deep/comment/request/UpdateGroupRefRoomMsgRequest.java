package com.hupu.deep.comment.request;

import com.hupu.deep.comment.types.CommentKey;
import lombok.Data;

/**
 * @author lisongqiu
 */
@Data
public class UpdateGroupRefRoomMsgRequest {

    private CommentKey commentKey;

    private String rongcloudRoomMsgId;

    private Long rongcloudRoomMsgTime;
}
