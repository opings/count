package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.SplitCountSubjectDO;
import org.apache.ibatis.annotations.Param;

/**
 * @author jiangfangyuan
 * @since 2020-03-12 13:31
 */
public interface SplitCountSubjectMapper {

    void insertSelective(SplitCountSubjectDO splitCountSubjectDO);

    void updateCountValue(@Param("subjectId") String subjectId, @Param("countValue") int countValue);

    void increaseCountValue(@Param("subjectId") String subjectId, @Param("countValue") int countValue);

    SplitCountSubjectDO selectBySubjectId(@Param("subjectId") String subjectId);


}
