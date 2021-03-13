package com.hupu.deep.score.request;

import com.hupu.deep.BaseRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yuepenglei
 * @Description 主体打分记录请求参数
 * @Date 2020/03/13 13:39
 */
@Data
public class SubjectScoreRecordListRequest extends BaseRequest implements Serializable {

    /**
     * 用户Id 集合
     */
    private List<Long> userIds;


    /**
     * 主体ID
     */
    private String subjectId;

}
