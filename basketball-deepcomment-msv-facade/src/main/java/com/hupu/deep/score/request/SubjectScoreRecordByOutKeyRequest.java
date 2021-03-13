package com.hupu.deep.score.request;

import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mayifan0701
 * @since 2020/3/19 17:46
 */
@Data
public class SubjectScoreRecordByOutKeyRequest implements Serializable {
    private static final long serialVersionUID = 4239917055053202982L;

    private Long userId;

    private OutBizKey outBizKey;
}
