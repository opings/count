package com.hupu.deep.light.request;

import com.hupu.deep.BaseRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuepenglei
 * @since 2020-03-17 16:47
 */
@Data
public class LightSubjectListRequest extends BaseRequest implements Serializable {

    /**
     * 主体 Id 集合
     */
    private List<String> subjectIdList;

}
