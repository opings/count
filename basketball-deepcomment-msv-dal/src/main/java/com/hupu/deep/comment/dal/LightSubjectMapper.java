package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.LightSubjectDO;
import org.apache.ibatis.annotations.Param;

/**
 * @author jiangfangyuan
 * @since 2020-03-12 13:31
 */
public interface LightSubjectMapper {

    void insertSelective(LightSubjectDO lightSubjectDO);

    LightSubjectDO selectByOutBizKey(@Param("outBizType") String outBizType, @Param("outBizNo") String outBizNo);

    LightSubjectDO selectBySubjectId(@Param("subjectId") String subjectId);


}
