package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.BlackSubjectDO;
import org.apache.ibatis.annotations.Param;

/**
 * @author jiangfangyuan
 * @since 2020-03-12 13:31
 */
public interface BlackSubjectMapper {

    void insertSelective(BlackSubjectDO blackSubjectDO);

    BlackSubjectDO selectByOutBizKey(@Param("outBizType") String outBizType, @Param("outBizNo") String outBizNo);

    BlackSubjectDO selectBySubjectId(@Param("subjectId") String subjectId);


}
