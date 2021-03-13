package com.hupu.deep.score.request;

import com.hupu.deep.BaseRequest;
import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mayifan0701
 * @since 2020/3/19 11:25
 */
@Data
public class SubjectScoreListRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = -9010712182506915988L;

    @NotNull
    @Size(max = 50)
    private List<OutBizKey> outBizKeyList;
}
