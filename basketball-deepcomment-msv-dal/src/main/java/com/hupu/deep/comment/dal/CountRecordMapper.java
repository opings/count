package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.CountRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 计数流水
 *
 * @author jiangfangyuan
 * @since 2020-03-15 23:10
 */
public interface CountRecordMapper {

    int insertSelective(CountRecordDO record);

    List<CountRecordDO> selectListBySubjectId(@Param("subjectId") String subjectId);

    CountRecordDO selectByIdempotentNo(@Param("subjectId") String subjectId, @Param("idempotentNo") String idempotentNo);

    CountRecordDO selectByIdempotentNoForUpdate(@Param("subjectId") String subjectId, @Param("idempotentNo") String idempotentNo);

    int updateCountValue(@Param("subjectId") String subjectId, @Param("idempotentNo") String idempotentNo, @Param("countValue") Integer countValue);

}
