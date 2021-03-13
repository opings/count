package com.hupu.deep.comment.request;

import com.hupu.deep.BaseRequest;
import com.hupu.deep.comment.types.CommentKey;
import lombok.Data;

/**
 * @author jiangfangyuan
 * @since 2020-03-18 13:36
 */
@Data
public class BlackRequest extends BaseRequest {


    private CommentKey commentKey;

}
