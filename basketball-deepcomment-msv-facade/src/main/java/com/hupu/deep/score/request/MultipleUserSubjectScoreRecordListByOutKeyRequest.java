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
public class MultipleUserSubjectScoreRecordListByOutKeyRequest implements Serializable {

    private static final long serialVersionUID = 2431782447909722857L;

    private List<Long> userIds;

    private OutBizKey outBizKey;
}
