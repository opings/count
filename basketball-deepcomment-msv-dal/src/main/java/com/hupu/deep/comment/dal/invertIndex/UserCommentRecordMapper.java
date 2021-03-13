package com.hupu.deep.comment.dal.invertIndex;

import com.hupu.deep.comment.entity.CommentRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCommentRecordMapper {


    int insert(CommentRecordDO record);


    CommentRecordDO selectByCommentId(@Param("userId") Long userId, @Param("subjectId") String subjectId, @Param("commentId") String commentId);


    List<CommentRecordDO> commentRecordList(@Param("userId") Long userId, @Param("auditStatusList") List<String> auditStatusList, @Param("limit") Integer limit);

    /**
     * 更新机器审核状态
     *
     * @param commentId
     * @param auditStatus
     * @return
     */
    int updateMachineAuditStatus(@Param("userId") Long userId, @Param("subjectId") String subjectId, @Param("commentId") String commentId,
                                 @Param("auditStatus") String auditStatus);


    /**
     * 更新人工审核状态
     *
     * @param commentId
     * @param auditStatus
     * @return
     */
    int updateManualAuditStatus(@Param("userId") Long userId, @Param("subjectId") String subjectId, @Param("commentId") String commentId,
                                @Param("auditStatus") String auditStatus, @Param("auditUserName") String auditUserName);

    /**
     * 更新最终审核状态
     *
     * @param commentId
     * @param auditStatus
     * @return
     */
    int updateFinalAuditStatus(@Param("userId") Long userId, @Param("subjectId") String subjectId, @Param("commentId") String commentId,
                               @Param("auditStatus") String auditStatus);
}