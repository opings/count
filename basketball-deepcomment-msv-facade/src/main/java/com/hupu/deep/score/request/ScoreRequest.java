package com.hupu.deep.score.request;

import com.hupu.deep.BaseRequest;
import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

/**
 * @Author yuepenglei
 * @Description 打分入参
 * @Date 2020/03/13 13:39
 */
@Data
public class ScoreRequest extends BaseRequest {

    /**
     * 打分分数
     */
    private Integer score;

    /**
     * 外部业务单据号
     */
    private OutBizKey outBizKey;

}
