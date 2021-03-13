package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.ScoreRecordDO;
import org.apache.ibatis.annotations.Param;

public interface ScoreRecordMapper {

    int insert(ScoreRecordDO record);

    ScoreRecordDO selectByPrimaryKey(Long id);

    ScoreRecordDO selectByScoreRecordId(String scoreRecordId);

    int updateByScoreRecordIdSelective(ScoreRecordDO record);

    /**
     * 获取打分记录
     *
     * @param userId 用户ID
     * @param subjectId 主体ID
     * @return
     */
    ScoreRecordDO getScoreRecord(@Param("userId") Long userId, @Param("subjectId") String subjectId);


    /**
     * 新增打分记录
     *
     * @param scoreRecordDO 实体
     */
    void saveScoreRecord(ScoreRecordDO scoreRecordDO);

    /**
     * 更新打分记录
     * @param userId 用户ID
     * @param subjectId 主体ID
     * @param score 分值
     */
    void updateScoreRecord(@Param("userId") Long userId, @Param("subjectId") String subjectId, @Param("score") Integer score);


}