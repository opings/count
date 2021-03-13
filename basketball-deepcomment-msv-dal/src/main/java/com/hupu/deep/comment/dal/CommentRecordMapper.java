package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.CommentRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommentRecordMapper {

    int insert(CommentRecordDO record);

    /**
     * 关联聊天室消息id
     * @param subjectId
     * @param commentId
     * @param rongcloudRoomMsgId
     * @param rongcloudRoomMsgTime
     * @return
     */
    int updateRongcloudGroupRefRoomMsg(@Param("subjectId") String subjectId,
                                 @Param("commentId") String commentId,
                                 @Param("rongcloudRoomMsgId") String rongcloudRoomMsgId,
                                 @Param("rongcloudRoomMsgTime") Long rongcloudRoomMsgTime);

    /**
     * 关联话题
     * @param subjectId
     * @param chatGroupTopicId
     * @return
     */
    int updateChatGroupTopicId(@Param("subjectId") String subjectId,
                                  @Param("rongcloudMsgIdList") List<String> rongcloudMsgIdList,
                                            @Param("chatGroupTopicId") String chatGroupTopicId);

    /**
     * 更改话题消息内容
     * @param subjectId
     * @param commentId
     * @param commentContentExtend
     * @return
     */
    int updateCommentContentExtend(@Param("subjectId") String subjectId,
                               @Param("commentId") String commentId,
                               @Param("commentContentExtend") String commentContentExtend);


    /**
     * 查询评论
     *
     * @param subjectId
     * @param commentId
     * @return
     */
    CommentRecordDO selectByCommentId(@Param("subjectId") String subjectId, @Param("commentId") String commentId);

    /**
     * 根据融云消息id查询
     *
     * @param subjectId
     * @param rongcloudMsgId
     * @return
     */
    CommentRecordDO selectByRongcloudMsgId(@Param("subjectId") String subjectId, @Param("rongcloudMsgId") String rongcloudMsgId);

     /**
     * 根据融云消息id查询
     *
     * @param subjectId
     * @param rongcloudMsgIdList
     * @return
     */
    List<CommentRecordDO> selectByRongcloudMsgIdList(@Param("subjectId") String subjectId, @Param("rongcloudMsgIdList") List<String> rongcloudMsgIdList);

    /**
     * 统计评论数
     *
     * @param subjectId
     * @param publishTime
     * @return
     */
    Long countBySubjectId(@Param("subjectId") String subjectId, @Param("publishTime") Long publishTime);

    /**
     * 统计评论人数
     *
     * @param subjectId
     * @param publishTime
     * @return
     */
    Long countUserIdBySubjectId(@Param("subjectId") String subjectId, @Param("publishTime") Long publishTime);

    /**
     * 统计评论数
     *
     * @param subjectId
     * @param publishTime
     * @return
     */
    Long countBySubjectIdAndChatGroupTopicId(@Param("subjectId") String subjectId,
                                             @Param("chatGroupTopicId") String chatGroupTopicId,
                                             @Param("publishTime") Long publishTime);

    /**
     * 统计评论人数
     *
     * @param subjectId
     * @param publishTime
     * @return
     */
    Long countUserBySubjectIdAndChatGroupTopicId(@Param("subjectId") String subjectId,
                                                   @Param("chatGroupTopicId") String chatGroupTopicId,
                                                   @Param("publishTime") Long publishTime);


    /**
     * 查询评论 lock 读
     *
     * @param subjectId
     * @param commentId
     * @return
     */
    CommentRecordDO selectByCommentIdForUpdate(@Param("subjectId") String subjectId, @Param("commentId") String commentId);


    /**
     * 更新机器审核状态
     *
     * @param commentId
     * @param auditStatus
     * @return
     */
    int updateMachineAuditStatus(@Param("subjectId") String subjectId, @Param("commentId") String commentId,
                                 @Param("auditStatus") String auditStatus);


    /**
     * 机器审核请求唯一标识
     *
     * @param subjectId
     * @param commentId
     * @param machineAuditRequestId
     * @return
     */
    int updateMachineAuditRequestId(@Param("subjectId") String subjectId, @Param("commentId") String commentId,
                                    @Param("machineAuditRequestId") String machineAuditRequestId);

    /**
     * 更新人工审核状态
     *
     * @param commentId
     * @param auditStatus
     * @return
     */
    int updateManualAuditStatus(@Param("subjectId") String subjectId, @Param("commentId") String commentId,
                                @Param("auditStatus") String auditStatus, @Param("auditUserName") String auditUserName);

    /**
     * 更新最终审核状态
     *
     * @param commentId
     * @param auditStatus
     * @return
     */
    int updateFinalAuditStatus(@Param("subjectId") String subjectId, @Param("commentId") String commentId,
                               @Param("auditStatus") String auditStatus);


    /**
     * 更新点亮数
     *
     * @param subjectId
     * @param commentId
     * @param lightCount
     * @return
     */
    int updateLightCount(@Param("subjectId") String subjectId, @Param("commentId") String commentId,
                         @Param("lightCount") Integer lightCount);

    /**
     * 更新点灭数
     *
     * @param subjectId
     * @param commentId
     * @param blackCount
     * @return
     */
    int updateBlackCount(@Param("subjectId") String subjectId, @Param("commentId") String commentId,
                         @Param("blackCount") Integer blackCount);


    /**
     * 根据评论id列表查询评论列表
     *
     * @param subjectId
     * @param commentIdList
     * @return
     */
    List<CommentRecordDO> selectByCommentIdList(@Param("subjectId") String subjectId, @Param("commentIdList") List<String> commentIdList);


    /**
     * 根据游标查询评论列表
     *
     * @param subjectId
     * @param publishTime
     * @param limit
     * @return
     */
    List<CommentRecordDO> selectByCursor(@Param("subjectId") String subjectId,
                                         @Param("publishTime") Long publishTime,
                                         @Param("limit") Integer limit,
                                         @Param("commentStatusList") List<String> commentStatusList);


    /**
     * 查询话题的消息
     * @param subjectId
     * @param chatGroupTopicId
     * @return
     */
    List<CommentRecordDO> selectByChatGroupTopicId(@Param("subjectId") String subjectId,
                                                   @Param("chatGroupTopicId") String chatGroupTopicId,
                                                   @Param("publishTime") Long publishTime,
                                                   @Param("limit") Integer limit,
                                                   @Param("commentStatusList") List<String> commentStatusList);


    /**
     * 查询cc的评论列表
     *
     * @param subjectId
     * @param userId
     * @param publishTime
     * @param limit
     * @return
     */
    List<CommentRecordDO> selectByCC(@Param("subjectId") String subjectId,
                                     @Param("userId") Long userId,
                                     @Param("publishTime") Long publishTime,
                                     @Param("limit") Integer limit);


    List<CommentRecordDO> selectCommentPageBySubjectId(@Param("subjectId") String subjectId,
                                                       @Param("startIndex") Integer startIndex,
                                                       @Param("limit") Integer limit);


    List<CommentRecordDO> selectPassedCommentBySubjectId(@Param("subjectId") String subjectId,
                                                         @Param("startIndex") Integer startIndex,
                                                         @Param("limit") Integer limit);

    Integer countCommentBySubjectId(@Param("subjectId") String subjectId);

    Integer countCommentUserBySubjectId(@Param("subjectId") String subjectId);

    /**
     * uv统计
     *
     * @param subjectId
     * @param contentList
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Map<String, String>> statsUV(@Param("subjectId") String subjectId, @Param("contentList") List<String> contentList,
                                      @Param("beginTime") Long beginTime, @Param("endTime") Long endTime);

    /**
     *
     * @param subjectId
     * @return
     */
    List<Long> selectUserIdBySubjectId(@Param("subjectId") String subjectId);


    /**
     *
     * @param subjectId
     * @param publishTime
     * @return
     */
    List<Long> selectUserIdBySubjectIdAndPublishTime(@Param("subjectId") String subjectId,
                                                     @Param("publishTime") Long publishTime);
}