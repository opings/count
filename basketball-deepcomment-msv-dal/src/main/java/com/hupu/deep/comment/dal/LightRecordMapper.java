package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.LightRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LightRecordMapper {

    /**
     * 获取点亮记录
     *
     * @param userId
     * @param subjectId
     * @return
     */
    LightRecordDO getLightRecord(@Param("userId") Long userId, @Param("subjectId") String subjectId
    );


    /**
     * 新增点亮记录
     *
     * @param lightRecordDO
     */
    void saveLightRecord(LightRecordDO lightRecordDO);

    /**
     * 点亮
     *
     * @param userId
     * @param subjectId
     */
    int light(@Param("userId") Long userId, @Param("subjectId") String subjectId
    );


    /**
     * 取消点亮
     *
     * @param userId
     * @param subjectId
     */
    int cancelLight(@Param("userId") Long userId, @Param("subjectId") String subjectId
    );

    /**
     * 统计贡献计数
     *
     * @param subjectId
     * @return
     */
    int statsContributionCount(@Param("subjectId") String subjectId);

    /**
     * 清除贡献计数
     *
     * @param subjectId
     * @return
     */
    int doneContributionCount(@Param("subjectId") String subjectId);

    /**
     *
     * @param subjectId
     * @return
     */
    List<LightRecordDO> selectLightRecord(@Param("subjectId") String subjectId);


}