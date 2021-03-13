package com.hupu.deep.comment.request;

import com.hupu.deep.BaseRequest;
import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

/**
 * @author jiangfangyuan
 * @since 2020-03-22 16:33
 */
@Data
public class BaseCommentRequest extends BaseRequest {


    /**
     * 主体 ID
     */
    private String subjectId;

    /**
     * 外部单据号
     */
    private OutBizKey outBizKey;
}
