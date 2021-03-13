package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.BlackRecordDO;
import org.apache.ibatis.annotations.Param;

public interface BlackRecordMapper {

    /**
     * 获取点灭记录
     *
     * @param userId
     * @param subjectId
     * @return
     */
    BlackRecordDO getBlackRecord(@Param("userId") Long userId, @Param("subjectId") String subjectId
    );


    /**
     * 新增点灭记录
     *
     * @param blackRecordDO
     */
    void saveBlackRecord(BlackRecordDO blackRecordDO);

    /**
     * 点灭
     *
     * @param userId
     * @param subjectId
     */
    int black(@Param("userId") Long userId, @Param("subjectId") String subjectId
    );


    /**
     * 取消点灭
     *
     * @param userId
     * @param subjectId
     */
    int cancelBlack(@Param("userId") Long userId, @Param("subjectId") String subjectId
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


}