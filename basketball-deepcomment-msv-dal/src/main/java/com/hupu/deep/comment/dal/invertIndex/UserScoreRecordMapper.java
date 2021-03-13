package com.hupu.deep.comment.dal.invertIndex;

import com.hupu.deep.comment.entity.ScoreRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserScoreRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ScoreRecordDO record);

    int insertSelective(ScoreRecordDO record);

    ScoreRecordDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ScoreRecordDO record);

    int updateByPrimaryKey(ScoreRecordDO record);

    /**
     * 获取打分记录
     *
     * @param userId 用户ID
     * @param subjectId 主体ID
     * @return
     */
    ScoreRecordDO getScoreRecord(@Param("userId") Long userId, @Param("subjectId") String subjectId);

    /**
     * 单用户一个主体下所有评分
     * @param userId
     * @param subjectIds
     * @return
     */
    List<ScoreRecordDO> getSingleUserScoreRecordList(@Param("userId") Long userId, @Param("subjectIds") List<String> subjectIds);

    /**
     * 一个主体下所有用户评分
     * @param userIds
     * @param subjectId
     * @return
     */
    List<ScoreRecordDO> getMultipleUserScoreRecordList(@Param("userIds") List<Long> userIds, @Param("subjectId") String subjectId);


    ScoreRecordDO getScoreRecordForUpdate(@Param("userId") Long userId, @Param("subjectId") String subjectId);


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