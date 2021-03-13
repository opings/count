package com.hupu.deep.comment.facade;

import com.hupu.deep.comment.CommentListDTO;
import com.hupu.deep.comment.model.CommentModel;
import com.hupu.deep.comment.request.*;
import com.hupu.deep.comment.types.CommentKey;
import com.hupu.foundation.error.CommonBizException;
import com.hupu.foundation.result.SimpleResult;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author jiangfangyuan
 * @since 2020-03-14 01:22
 */
@FeignClient(name = "basketball-deepcomment-msv")
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface CommentFacade {


    /**
     * 发布评论
     * 业务异常:用户被封禁
     *
     * @param request
     * @return
     * @throws CommonBizException
     */
    @PostMapping("/deep/comment/publish")
    SimpleResult<CommentKey> publishComment(@RequestBody PublishCommentRequest request) throws CommonBizException;


    /**
     * 获取单个评论
     *
     * @param commentKey
     * @return
     */
    @GetMapping("/deep/comment/info")
    SimpleResult<CommentModel> commentInfo(@RequestBody CommentKey commentKey);

    /**
     * 获取单个评论
     *
     * @param subjectId
     * @param rongcloudMsgId
     * @return
     */
    @GetMapping("/deep/comment/infoByRongcloudMsgId")
    SimpleResult<CommentModel> commentInfoByRongcloudMsgId(@RequestParam("subjectId") String subjectId, @RequestParam("rongcloudMsgId") String rongcloudMsgId);

    /**
     * 将融云群消息id关联群聊天室消息id
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/comment/updateGroupRefRoomMsg")
    SimpleResult<String> updateGroupRefRoomMsg(@RequestBody UpdateGroupRefRoomMsgRequest request);

    /**
     * 获取评论
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/infoByRongcloudMsgIdList")
    SimpleResult<List<CommentModel>> commentInfoByRongcloudMsgIdList(@RequestBody CommentInfoByRongcloudMsgIdListRequest request);

    /**
     * 点亮
     *
     * @param lightRequest
     * @return
     */
    @PostMapping("/deep/comment/light")
    SimpleResult<String> light(@RequestBody LightRequest lightRequest);

    /**
     * 取消点亮
     *
     * @param lightRequest
     * @return
     */
    @PostMapping("/deep/comment/cancelLight")
    SimpleResult<String> cancelLight(@RequestBody LightRequest lightRequest);

    /**
     * 是否点亮
     *
     * @param lightRequest
     * @return
     */
    @GetMapping("/deep/comment/hasLight")
    SimpleResult<String> hasLight(@RequestBody LightRequest lightRequest);


    /**
     * 点灭
     *
     * @param lightRequest
     * @return
     */
    @PostMapping("/deep/comment/black")
    SimpleResult<String> black(@RequestBody BlackRequest lightRequest);

    /**
     * 取消点灭
     *
     * @param lightRequest
     * @return
     */
    @PostMapping("/deep/comment/cancelBlack")
    SimpleResult<String> cancelBlack(@RequestBody BlackRequest lightRequest);

    /**
     * 是否点灭
     *
     * @param lightRequest
     * @return
     */
    @GetMapping("/deep/comment/hasBlack")
    SimpleResult<String> hasBlack(@RequestBody BlackRequest lightRequest);

    /**
     * 点灭数
     *
     * @param commentKey
     * @return
     */
    @GetMapping("/deep/comment/blackCount")
    SimpleResult<Integer> blackCount(@RequestBody CommentKey commentKey);

    /**
     * 评论列表
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/list")
    SimpleResult<CommentListDTO> commentList(@RequestBody CommentListRequest request);

    /**
     * 团话题消息列表
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/commentListByChatGroupTopicId")
    SimpleResult<CommentListDTO> commentListByChatGroupTopicId(@RequestBody CommentListByChatGroupTopicIdRequest request);

    /**
     * 评论列表
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/comment/listForPost")
    SimpleResult<CommentListDTO> commentListForPost(@RequestBody CommentListRequest request);


    /**
     * cc 的评论列表
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/ccComment/list")
    SimpleResult<List<CommentModel>> ccCommentList(@RequestBody CcCommentListRequest request);

    /**
     * 获取点亮数
     *
     * @param commentKey
     * @return
     */
    @GetMapping("/deep/comment/lightCount")
    SimpleResult<Integer> lightCount(@RequestBody CommentKey commentKey);

    /**
     * 提供给marketing-api
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/commentPageList")
    SimpleResult<List<CommentModel>> commentPageList(@RequestBody CommentPageListRequest request);


    /**
     * 提供给marketing-api
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/countComment")
    SimpleResult<Integer> countComment(@RequestBody CountCommentRequest request);


    /**
     * 提供给marketing-api
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/comment/countCommentForPost")
    SimpleResult<Integer> countCommentForPost(@RequestBody CountCommentRequest request);

    /**
     * 提供给marketing-api
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/countCommentUser")
    SimpleResult<Integer> countCommentUser(@RequestBody CountCommentUserRequest request);


    /**
     * 提供给marketing-api
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/statsUV")
    SimpleResult<List<Map<String, String>>> statsUV(@RequestBody StatsUvRequest request);

    /**
     * 点亮用户id
     *
     * @param commentKey
     * @return
     */
    @GetMapping("/deep/comment/lightUserIdList")
    SimpleResult<List<Long>> lightUserIdList(@RequestBody CommentKey commentKey);

    /**
     * 删除评论
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/comment/deleteCC")
    SimpleResult<String> deleteCC(@RequestBody DeleteCCRequest request);

    /**
     * 用户评论列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/deep/comment/userCommentList")
    SimpleResult<List<CommentModel>> userCommentList(@RequestParam("userId") Long userId,
                                                     @RequestParam("limit") Integer limit);

    /**
     * @param subjectId
     * @param publishTime
     * @return
     */
    @GetMapping("/deep/comment/countBySubjectId")
    SimpleResult<Long> countBySubjectId(@RequestParam("subjectId") String subjectId, @RequestParam("publishTime") Long publishTime);

    /**
     * @param subjectId
     * @param publishTime
     * @return
     */
    @GetMapping("/deep/comment/countUserIdBySubjectId")
    SimpleResult<Long> countUserIdBySubjectId(@RequestParam("subjectId") String subjectId, @RequestParam("publishTime") Long publishTime);


    /**
     * @param subjectId
     * @param publishTime
     * @return
     */
    @GetMapping("/deep/comment/countBySubjectIdAndChatGroupTopicId")
    SimpleResult<Long> countBySubjectIdAndChatGroupTopicId(@RequestParam("subjectId") String subjectId,
                                                           @RequestParam("chatGroupTopicId") String chatGroupTopicId,
                                                           @RequestParam("publishTime") Long publishTime);

    /**
     * @param subjectId
     * @param publishTime
     * @return
     */
    @GetMapping("/deep/comment/countUserBySubjectIdAndChatGroupTopicId")
    SimpleResult<Long> countUserBySubjectIdAndChatGroupTopicId(@RequestParam("subjectId") String subjectId,
                                                               @RequestParam("chatGroupTopicId") String chatGroupTopicId,
                                                               @RequestParam("publishTime") Long publishTime);

    /**
     * 更新消息所属话题id
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/comment/updateChatGroupTopicId")
    SimpleResult<String> updateChatGroupTopicId(@RequestBody UpdateChatGroupTopicIdRequest request);


    /**
     * 更新话题消息内容
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/comment/updateCommentContentExtend")
    SimpleResult<String> updateCommentContentExtend(@RequestBody UpdateCommentContentExtendRequest request);

    @GetMapping("/deep/comment/getUserIdListBySubjectId")
    SimpleResult<List<Long>> getUserIdListBySubjectId(@RequestParam("subjectId") String subjectId);

    @GetMapping("/deep/comment/getUserIdListBySubjectIdAndPublishTime")
    SimpleResult<List<Long>> getUserIdListBySubjectIdAndPublishTime(@RequestParam("subjectId") String subjectId,
                                                                    @RequestParam("publishTime") Long publishTime);


}