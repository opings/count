package com.hupu.deep.comment.web.controller;


import com.google.common.collect.Lists;
import com.hupu.deep.comment.CommentListDTO;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.facade.CommentFacade;
import com.hupu.deep.comment.model.CommentModel;
import com.hupu.deep.comment.model.subject.SubjectModel;
import com.hupu.deep.comment.request.*;
import com.hupu.deep.comment.service.CommentAuditService;
import com.hupu.deep.comment.service.CommentService;
import com.hupu.deep.comment.service.CommentSubjectService;
import com.hupu.deep.comment.types.CommentKey;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.CacheConfig;
import com.hupu.foundation.Globals;
import com.hupu.foundation.cache.CacheClient;
import com.hupu.foundation.cache.FoundationCacheConfig;
import com.hupu.foundation.error.CommonBizException;
import com.hupu.foundation.result.SimpleResult;
import com.hupu.foundation.util.AssertUtil;
import com.hupu.foundation.util.DateUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 14:51:00
 */
@RestController
@Api("评论服务")
@Slf4j
public class CommentController implements CommentFacade {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentSubjectService commentSubjectService;
    @Autowired
    private CacheClient cacheClient;
    @Autowired
    private CommentAuditService commentAuditService;
    @Autowired
    private ConfigClient configClient;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private FoundationCacheConfig foundationCacheConfig;

    @Override
    public SimpleResult<CommentKey> publishComment(@RequestBody PublishCommentRequest request) throws CommonBizException {

        try {
            AssertUtil.notNull(request.getUserId(), () -> "userId null");
            String subjectId = getOrCreateSubjectId(request.getSubjectId(), request.getOutBizKey());
            SubjectModel subject = new SubjectModel(request.getOutBizKey(), subjectId);
            String commentId = commentService.publishComment(request.getClientId(), subject, request.getUserId(),
                    request.getParentCommentId(), request.getChatGroupTopicId(), request.getRongcloudMsgId(), request.getRongcloudMsgTime(), request.getContent(), request.getContentExtend());
            CommentKey commentKey = new CommentKey(subjectId, commentId);
            return SimpleResult.success(commentKey);
        } catch (CommonBizException ex) {
            return SimpleResult.failure(ex.getErrorCode(), ex.getErrorMsg());
        }
    }

    @Override
    public SimpleResult<CommentModel> commentInfo(@RequestBody CommentKey commentKey) {

        CommentModel commentModel = commentService.commentInfo(commentKey);
        return SimpleResult.success(commentModel);
    }

    @Override
    public SimpleResult<CommentModel> commentInfoByRongcloudMsgId(@RequestParam("subjectId") String subjectId, @RequestParam("rongcloudMsgId") String rongcloudMsgId) {
        CommentModel commentModel = commentService.commentInfo(subjectId, rongcloudMsgId);
        return SimpleResult.success(commentModel);
    }

    @Override
    public SimpleResult<String> updateGroupRefRoomMsg(@RequestBody UpdateGroupRefRoomMsgRequest request) {
        commentService.updateRongcloudGroupRefRoomMsg(request.getCommentKey(),
                request.getRongcloudRoomMsgId(),
                request.getRongcloudRoomMsgTime());
        return SimpleResult.success(Globals.Y);
    }

    @Override
    public SimpleResult<List<CommentModel>> commentInfoByRongcloudMsgIdList(@RequestBody CommentInfoByRongcloudMsgIdListRequest request) {
        return SimpleResult.success(commentService.commentInfoList(request.getSubjectId(), request.getRongcloudMsgIdList()));
    }

    @Override
    public SimpleResult<CommentListDTO> commentList(@RequestBody CommentListRequest request) {

        List<CommentModel> commentList =
                cacheClient.get(
                        CacheConfig.COMMENT,
                        request.toString(),
                        () -> {
                            if (request.getCommentCursor() == null) {
                                CommentCursor commentCursor = new CommentCursor();
                                commentCursor.setPublishTime(DateUtils.nowDate().getTime());
                                commentCursor.setLimit(com.hupu.deep.comment.util.Globals.NUM_40);
                                request.setCommentCursor(commentCursor);
                            }
                            return new ArrayList<>(commentService.commentList(
                                    getSubjectId(request.getSubjectId(), request.getOutBizKey()),
                                    request.getCommentIdList(),
                                    request.getCommentCursor(),
                                    request.getCommentStatusList()));
                        }, com.hupu.deep.comment.util.Globals.NUM_40);
        if (Objects.nonNull(request.getUserId())) {
            commentList = commentList.stream().filter(item -> item.canSee(request.getUserId()))
                    .collect(Collectors.toList());
        }
        CommentListDTO commentListDTO = new CommentListDTO();
        commentListDTO.setCommentModelList(commentList);
        if (CollectionUtils.isNotEmpty(commentList)) {
            CommentCursor commentCursor = new CommentCursor();
            commentCursor.setPublishTime(commentList.get(commentList.size() - Globals.ONE).getPublishTime());
            if (request.getCommentCursor() == null) {
                commentCursor.setLimit(com.hupu.deep.comment.util.Globals.NUM_40);
            } else {
                commentCursor.setLimit(request.getCommentCursor().getLimit());
            }
            commentListDTO.setCommentCursor(commentCursor);
        }
        return SimpleResult.success(commentListDTO);
    }


    @Override
    public SimpleResult<CommentListDTO> commentListByChatGroupTopicId(@RequestBody CommentListByChatGroupTopicIdRequest request) {

        List<CommentModel> commentList =
                cacheClient.get(
                        CacheConfig.COMMENT,
                        request.toString(),
                        () -> {
                            if (request.getCommentCursor() == null) {
                                CommentCursor commentCursor = new CommentCursor();
                                commentCursor.setPublishTime(DateUtils.nowDate().getTime());
                                commentCursor.setLimit(com.hupu.deep.comment.util.Globals.NUM_40);
                                request.setCommentCursor(commentCursor);
                            }
                            return new ArrayList<>(commentService.selectByChatGroupTopicId(
                                    getSubjectId(request.getSubjectId(), request.getOutBizKey()),
                                    request.getChatGroupTopicId(),
                                    request.getCommentCursor(),
                                    request.getCommentStatusList()));
                        }, com.hupu.deep.comment.util.Globals.NUM_40);
        if (Objects.nonNull(request.getUserId())) {
            commentList = commentList.stream().filter(item -> item.canSee(request.getUserId()))
                    .collect(Collectors.toList());
        }
        CommentListDTO commentListDTO = new CommentListDTO();
        commentListDTO.setCommentModelList(commentList);
        if (CollectionUtils.isNotEmpty(commentList)) {
            CommentCursor commentCursor = new CommentCursor();
            commentCursor.setPublishTime(commentList.get(commentList.size() - Globals.ONE).getPublishTime());
            if (request.getCommentCursor() == null) {
                commentCursor.setLimit(com.hupu.deep.comment.util.Globals.NUM_40);
            } else {
                commentCursor.setLimit(request.getCommentCursor().getLimit());
            }
            commentListDTO.setCommentCursor(commentCursor);
        }
        return SimpleResult.success(commentListDTO);
    }


    @Override
    public SimpleResult<CommentListDTO> commentListForPost(@RequestBody CommentListRequest request) {
        return commentList(request);
    }


    @Override
    public SimpleResult<List<CommentModel>> commentPageList(@RequestBody CommentPageListRequest request) {
        AssertUtil.notNull(request.getOutBizKey(), () -> "outBizKey null");
        AssertUtil.notNull(request.getStartIndex(), () -> "startIndex null");
        AssertUtil.notNull(request.getLimit(), () -> "limit null");
        String subjectId = getSubjectId(request.getSubjectId(), request.getOutBizKey());
        if (StringUtils.isBlank(subjectId)) {
            return SimpleResult.success(Lists.newArrayList());
        }
        List<CommentModel> commentList = commentService.selectCommentBySubjectId(subjectId, request.getStartIndex(), request.getLimit());
        return SimpleResult.success(commentList);
    }

    @Override
    public SimpleResult<Integer> countComment(@RequestBody CountCommentRequest request) {
        AssertUtil.notNull(request.getOutBizKey(), () -> "outBizKey null");
        String subjectId = getSubjectId(request.getSubjectId(), request.getOutBizKey());
        if (StringUtils.isBlank(subjectId)) {
            return SimpleResult.success(0);
        }
        Integer count = commentService.countCommentBySubjectId(subjectId);
        return SimpleResult.success(count);
    }

    @Override
    public SimpleResult<Integer> countCommentForPost(@RequestBody CountCommentRequest request) {
        return countComment(request);
    }

    @Override
    public SimpleResult<Integer> countCommentUser(@RequestBody CountCommentUserRequest request) {
        AssertUtil.notNull(request.getOutBizKey(), () -> "outBizKey null");
        String subjectId = getSubjectId(request.getSubjectId(), request.getOutBizKey());
        if (StringUtils.isBlank(subjectId)) {
            return SimpleResult.success(0);
        }
        Integer count = commentService.countCommentUserBySubjectId(subjectId);
        return SimpleResult.success(count);
    }

    @Override
    public SimpleResult<List<CommentModel>> ccCommentList(@RequestBody CcCommentListRequest request) {

        List<CommentModel> commentModelList = commentService.ccCommentList(request.getUserId(), getSubjectId(request.getSubjectId(), request.getOutBizKey()), request.getCommentCursor());
        return SimpleResult.success(commentModelList);
    }

    @Override
    public SimpleResult<Integer> lightCount(@RequestBody CommentKey commentKey) {
        return SimpleResult.success(commentService.getLightCount(commentKey));
    }

    @Override
    public SimpleResult<String> light(@RequestBody LightRequest lightRequest) {
        commentService.light(lightRequest.getUserId(), lightRequest.getCommentKey());
        return SimpleResult.success(Globals.Y);
    }

    @Override
    public SimpleResult<String> cancelLight(@RequestBody LightRequest lightRequest) {
        commentService.cancelLight(lightRequest.getUserId(), lightRequest.getCommentKey());
        return SimpleResult.success(Globals.Y);
    }

    @Override
    public SimpleResult<String> hasLight(@RequestBody LightRequest lightRequest) {
        boolean hasLight = commentService.hasLight(lightRequest.getUserId(), lightRequest.getCommentKey());
        return SimpleResult.success(hasLight ? Globals.Y : Globals.N);
    }

    @Override
    public SimpleResult<String> black(@RequestBody BlackRequest blackRequest) {
        commentService.black(blackRequest.getUserId(), blackRequest.getCommentKey());
        return SimpleResult.success(Globals.Y);
    }

    @Override
    public SimpleResult<String> cancelBlack(@RequestBody BlackRequest blackRequest) {
        commentService.cancelBlack(blackRequest.getUserId(), blackRequest.getCommentKey());
        return SimpleResult.success(Globals.Y);
    }

    @Override
    public SimpleResult<String> hasBlack(@RequestBody BlackRequest blackRequest) {
        boolean hasBlack = commentService.hasBlack(blackRequest.getUserId(), blackRequest.getCommentKey());
        return SimpleResult.success(hasBlack ? Globals.Y : Globals.N);
    }

    @Override
    public SimpleResult<Integer> blackCount(@RequestBody CommentKey commentKey) {
        return SimpleResult.success(commentService.getBlackCount(commentKey));
    }


    @Override
    public SimpleResult<List<Map<String, String>>> statsUV(@RequestBody StatsUvRequest request) {

        request.setSubjectId(getSubjectId(request.getSubjectId(), request.getOutBizKey()));

        List<Map<String, String>> uvList = commentService.statsUV(request.getSubjectId(),
                request.getContentList(),
                request.getBeginTime(),
                request.getEndTime());
        return SimpleResult.success(uvList);
    }


    private String getOrCreateSubjectId(String subjectId, OutBizKey outBizKey) {

        return Optional.ofNullable(StringUtils.defaultIfEmpty(getSubjectId(subjectId, outBizKey), null))
                .orElseGet(() -> commentSubjectService.createSubject(outBizKey));
    }

    private String getSubjectId(String subjectId, OutBizKey outBizKey) {

        if (StringUtils.isNotBlank(subjectId)) {
            return subjectId;
        }
        return commentSubjectService.getSubjectId(outBizKey);
    }

    @Override
    public SimpleResult<List<Long>> lightUserIdList(@RequestBody CommentKey commentKey) {
        return SimpleResult.success(commentService.getLightUserIdList(commentKey));
    }

    @Override
    public SimpleResult<String> deleteCC(@RequestBody DeleteCCRequest request) {
        commentAuditService.manualAuditDeleteComment(request.getCommentKey(), "recall");
        return SimpleResult.success(Globals.Y);
    }

    @Override
    public SimpleResult<List<CommentModel>> userCommentList(@RequestParam("userId") Long userId,
                                                            @RequestParam("limit") Integer limit) {
        return SimpleResult.success(commentService.userCommentList(userId, limit));
    }

    @Override
    public SimpleResult<Long> countBySubjectId(@RequestParam("subjectId") String subjectId, @RequestParam("publishTime") Long publishTime) {
        return SimpleResult.success(commentService.countBySubjectId(subjectId, publishTime));
    }

    @Override
    public SimpleResult<Long> countUserIdBySubjectId(@RequestParam("subjectId") String subjectId, @RequestParam("publishTime") Long publishTime) {
        return SimpleResult.success(commentService.countUserIdBySubjectId(subjectId, publishTime));
    }

    @Override
    public SimpleResult<Long> countBySubjectIdAndChatGroupTopicId(@RequestParam("subjectId") String subjectId,
                                                                  @RequestParam("chatGroupTopicId") String chatGroupTopicId,
                                                                  @RequestParam("publishTime") Long publishTime) {
        return SimpleResult.success(commentService.countBySubjectIdAndChatGroupTopicId(subjectId, chatGroupTopicId, publishTime));
    }

    @Override
    public SimpleResult<Long> countUserBySubjectIdAndChatGroupTopicId(@RequestParam("subjectId") String subjectId,
                                                                      @RequestParam("chatGroupTopicId") String chatGroupTopicId,
                                                                      @RequestParam("publishTime") Long publishTime) {
        return SimpleResult.success(commentService.countUserBySubjectIdAndChatGroupTopicId(subjectId, chatGroupTopicId, publishTime));
    }

    @Override
    public SimpleResult<String> updateChatGroupTopicId(@RequestBody UpdateChatGroupTopicIdRequest request) {
        AssertUtil.notBlank(request.getSubjectId(), () -> "subjectId empty");
        AssertUtil.notBlank(request.getChatGroupTopicId(), () -> "chatGroupTopicId empty");
        if (CollectionUtils.isEmpty(request.getRongcloudMsgIdList())) {
            throw new IllegalArgumentException("rongcloudMsgIdList empty");
        }
        commentService.updateChatGroupTopicId(request.getSubjectId(), request.getRongcloudMsgIdList(), request.getChatGroupTopicId());
        return SimpleResult.success(Globals.Y);
    }

    @Override
    public SimpleResult<String> updateCommentContentExtend(@RequestBody UpdateCommentContentExtendRequest request) {
        AssertUtil.notNull(request.getCommentKey(), () -> "commentKey null");
        AssertUtil.notBlank(request.getCommentContentExtend(), () -> "commentContentExtend empty");
        commentService.updateCommentContentExtend(request.getCommentKey().getSubjectId(), request.getCommentKey().getCommentId(), request.getCommentContentExtend());
        return SimpleResult.success(Globals.Y);
    }

    @Override
    public SimpleResult<List<Long>> getUserIdListBySubjectId(@RequestParam("subjectId") String subjectId) {
        return SimpleResult.success(commentService.getUserIdListBySubjectId(subjectId));
    }

    @Override
    public SimpleResult<List<Long>> getUserIdListBySubjectIdAndPublishTime(@RequestParam("subjectId") String subjectId,
                                                                           @RequestParam("publishTime") Long publishTime) {
        return SimpleResult.success(commentService.getUserIdListBySubjectIdAndPublishTime(subjectId,publishTime));
    }


}
