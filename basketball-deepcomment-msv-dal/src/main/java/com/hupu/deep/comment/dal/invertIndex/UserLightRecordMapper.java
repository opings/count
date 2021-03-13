package com.hupu.deep.comment.dal.invertIndex;

import com.hupu.deep.comment.entity.LightRecordDO;
import org.apache.ibatis.annotations.Param;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 00:30
 */
public interface UserLightRecordMapper {


    /**
     * 获取点亮记录
     *
     * @param userId
     * @param subjectId
     * @return
     */
    LightRecordDO getLightRecord(@Param("userId") Long userId,
                                 @Param("subjectId") String subjectId);

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
    void light(@Param("userId") Long userId, @Param("subjectId") String subjectId
    );


    /**
     * 取消点亮
     *
     * @param userId
     * @param subjectId
     */
    void cancelLight(@Param("userId") Long userId, @Param("subjectId") String subjectId
    );


}
