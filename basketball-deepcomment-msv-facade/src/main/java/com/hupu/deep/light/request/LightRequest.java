package com.hupu.deep.light.request;

import com.hupu.deep.BaseRequest;
import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 16:03
 */
@Data
public class LightRequest extends BaseRequest {


    private OutBizKey outBizKey;
}
