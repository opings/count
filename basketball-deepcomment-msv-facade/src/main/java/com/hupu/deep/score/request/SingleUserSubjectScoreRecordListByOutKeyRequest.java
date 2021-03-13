package com.hupu.deep.score.request;

import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mayifan0701
 * @since 2020/3/19 17:46
 */
@Data
public class SingleUserSubjectScoreRecordListByOutKeyRequest implements Serializable {

    private static final long serialVersionUID = 8634560100907788236L;

    private Long userId;

    private List<OutBizKey> outBizKeyList;
}
