package com.hupu.deep.comment.dal.invertIndex;

import com.hupu.deep.comment.entity.BlackRecordDO;
import org.apache.ibatis.annotations.Param;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 00:30
 */
public interface UserBlackRecordMapper {


    /**
     * 获取点灭记录
     *
     * @param userId
     * @param subjectId
     * @return
     */
    BlackRecordDO getBlackRecord(@Param("userId") Long userId,
                                 @Param("subjectId") String subjectId);

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
    void black(@Param("userId") Long userId, @Param("subjectId") String subjectId
    );


    /**
     * 取消点灭
     *
     * @param userId
     * @param subjectId
     */
    void cancelBlack(@Param("userId") Long userId, @Param("subjectId") String subjectId
    );


}
