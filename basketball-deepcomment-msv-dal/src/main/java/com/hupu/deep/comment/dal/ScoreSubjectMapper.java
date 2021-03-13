package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.ScoreSubjectDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScoreSubjectMapper {


    int insertSelective(ScoreSubjectDO record);


    ScoreSubjectDO selectBySubjectId(String subjectId);

    List<ScoreSubjectDO> selectBySubjectIds(@Param("subjectIds") List<String> subjectIds);

    /**
     * 获取打分主体
     *
     * @param outBizType 外部业务类型
     * @param outBizNo   外部业务id
     * @return
     */
    ScoreSubjectDO selectByOutBizKey(@Param("outBizType") String outBizType, @Param("outBizNo") String outBizNo);
}