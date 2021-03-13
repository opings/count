package com.hupu.deep.comment.service;

import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.google.common.collect.Maps;
import com.hupu.deep.black.service.BlackService;
import com.hupu.deep.black.service.BlackSubjectService;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.client.mq.MqClient;
import com.hupu.deep.comment.dal.CommentRecordMapper;
import com.hupu.deep.comment.dal.invertIndex.UserCommentRecordMapper;
import com.hupu.deep.comment.entity.CommentRecordDO;
import com.hupu.deep.comment.enums.AuditStatusEnum;
import com.hupu.deep.comment.enums.BizTypeEnum;
import com.hupu.deep.comment.helper.CommentHelper;
import com.hupu.deep.comment.model.CommentModel;
import com.hupu.deep.comment.model.subject.SubjectModel;
import com.hupu.deep.comment.request.CommentCursor;
import com.hupu.deep.comment.types.CommentKey;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.comment.util.MqEventCode;
import com.hupu.deep.comment.util.error.ErrorCode;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.deep.light.service.LightService;
import com.hupu.deep.light.service.LightSubjectService;
import com.hupu.foundation.error.CommonBizException;
import com.hupu.foundation.error.CommonSystemException;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.fn.Pausable;
import reactor.fn.timer.HashWheelTimer;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 22:08:00
 */
@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRecordMapper commentRecordMapper;
    @Autowired
    private MqClient mqClient;
    @Autowired
    private CommentHelper commentHelper;
    @Autowired
    private ConfigClient configClient;
    @Autowired
    private IdService idService;
    @Autowired
    private LightSubjectService lightSubjectService;
    @Autowired
    private LightService lightService;
    @Autowired
    private BlackSubjectService blackSubjectService;
    @Autowired
    private BlackService blackService;
    @Autowired
    private UserCommentRecordMapper userCommentRecordMapper;

    private HashWheelTimer hashWheelTimer = new HashWheelTimer(1000, (int) Math.pow(2, 10),
            new HashWheelTimer.SleepWait());
    private Map<CommentKey, Pausable> lightCommentMap = Maps.newConcurrentMap();

    private Map<CommentKey, Pausable> blackCommentMap = Maps.newConcurrentMap();


    /**
     * 发布评论
     * 如果后续 业务规则复杂 可以考虑 责任链
     *
     * @param clientId
     * @param subjectModel
     * @param userId
     * @param parentCommentId
     * @param content
     * @return
     */
    public String publishComment(Long clientId, SubjectModel subjectModel, Long userId, String parentCommentId,
                                 String chatGroupTopicId, String rongcloudMsgId, Long rongcloudMsgTime, String content,
                                 String contentExtendJsonStr) throws CommonBizException {
        String subjectId = subjectModel.getSubjectId();
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(content, () -> "content empty");

        //checkPublishCommentRight(userId, subjectModel, parentCommentId, clientId);

        String commentId = idService.genId(BizTypeEnum.COMMENT.getCode(), userId, Globals.DB_SIZE).toString();
        CommentKey commentKey = new CommentKey(subjectId, commentId);
        String finalRongcloudMsgId = StringUtils.isNotBlank(rongcloudMsgId) ? rongcloudMsgId : commentId;
        Long finalRongcloudMsgTime = Objects.nonNull(rongcloudMsgTime) ? rongcloudMsgTime : System.currentTimeMillis();
        String finalChatGroupTopicId = StringUtils.isNotBlank(chatGroupTopicId) ? chatGroupTopicId : String.valueOf(Globals.NUM_0);
        mqClient.sendTransactionMsg(configClient.getCommentTopic(), MqEventCode.EC_PUBLISH_COMMENT, commentKey, (msg, arg) -> {
            try {
                CommentRecordDO commentRecordDO = commentHelper.buildCommentRecordDO(commentKey,
                        parentCommentId, finalChatGroupTopicId, finalRongcloudMsgId, finalRongcloudMsgTime, userId, content, contentExtendJsonStr);
                commentRecordMapper.insert(commentRecordDO);
            } catch (Exception ex) {
                log.error("publishComment error " + commentKey, ex);
                return TransactionStatus.RollbackTransaction;
            }
            return TransactionStatus.CommitTransaction;
        }, commentKey);
        return commentId;
    }

    /**
     * @param commentKey
     * @param rongcloudRoomMsgId
     * @param rongcloudRoomMsgTime
     */
    public void updateRongcloudGroupRefRoomMsg(CommentKey commentKey,
                                               String rongcloudRoomMsgId,
                                               Long rongcloudRoomMsgTime) {
        commentRecordMapper.updateRongcloudGroupRefRoomMsg(commentKey.getSubjectId(), commentKey.getCommentId(), rongcloudRoomMsgId, rongcloudRoomMsgTime);
    }

    /**
     * 更改消息所属话题id
     *
     * @param subjectId
     * @param rongcloudMsgIdList
     * @param chatGroupTopicId
     */
    public void updateChatGroupTopicId(String subjectId,
                                       List<String> rongcloudMsgIdList,
                                       String chatGroupTopicId) {
        commentRecordMapper.updateChatGroupTopicId(subjectId, rongcloudMsgIdList, chatGroupTopicId);
    }

    /**
     * 更新话题消息内容
     *
     * @param subjectId
     * @param commentId
     * @param commentContentExtend
     */
    public void updateCommentContentExtend(String subjectId,
                                           String commentId,
                                           String commentContentExtend) {
        commentRecordMapper.updateCommentContentExtend(subjectId, commentId, commentContentExtend);
    }


    /**
     * 查询单个评论
     *
     * @param commentKey
     * @return
     */
    public CommentModel commentInfo(CommentKey commentKey) {

        AssertUtil.notNull(commentKey, () -> "commentKey null");

        CommentRecordDO commentRecordDO = commentRecordMapper.selectByCommentId(commentKey.getSubjectId(), commentKey.getCommentId());
        CommentModel commentModel = commentHelper.buildCommentModel(commentRecordDO);
        if (null == commentModel) {
            return null;
        }
        return fillLightCount(fillBlackCount(commentModel));
    }

    /**
     * @param subjectId
     * @param rongcloudMsgId
     * @return
     */
    public CommentModel commentInfo(String subjectId, String rongcloudMsgId) {
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notBlank(rongcloudMsgId, () -> "rongcloudMsgId empty");
        CommentRecordDO commentRecordDO = commentRecordMapper.selectByRongcloudMsgId(subjectId, rongcloudMsgId);
        CommentModel commentModel = commentHelper.buildCommentModel(commentRecordDO);
        if (null == commentModel) {
            return null;
        }
        return commentModel;
    }

    public List<CommentModel> commentInfoList(String subjectId, List<String> rongcloudMsgIdList) {
        AssertUtil.notBlank(subjectId, () -> "commentKey empty");
        AssertUtil.notNull(rongcloudMsgIdList, () -> "rongcloudMsgIdList empty");
        AssertUtil.isTrue(!CollectionUtils.isEmpty(rongcloudMsgIdList), () -> "rongcloudMsgIdList empty");
        List<CommentRecordDO> commentRecordList = commentRecordMapper.selectByRongcloudMsgIdList(subjectId, rongcloudMsgIdList);
        return commentRecordList.stream().map(commentRecordDO ->
                commentHelper.buildCommentModel(commentRecordDO))
                .collect(Collectors.toList());
    }

    /**
     * 用户所有评论
     *
     * @param userId
     * @param limit
     * @return
     */
    public List<CommentModel> userCommentList(Long userId, Integer limit) {
        AssertUtil.notNull(userId, () -> "userId empty");
        List<CommentRecordDO> userCommentRecordList =
                userCommentRecordMapper.commentRecordList(userId, Lists.newArrayList(
                        AuditStatusEnum.WAITING_AUDIT.getCode(),
                        AuditStatusEnum.AUDIT_PASS.getCode(),
                        AuditStatusEnum.AUDIT_NOT_PASS.getCode()), limit);

        List<CommentRecordDO> commentRecordList = new ArrayList<>();
        userCommentRecordList.forEach(userCommentRecordDO -> {
            CommentRecordDO commentRecord = commentRecordMapper.selectByCommentId(userCommentRecordDO.getSubjectId(), userCommentRecordDO.getCommentId());
            if (commentRecord != null) {
                commentRecordList.add(commentRecord);
            }
        });
        return commentRecordList.stream().map(commentRecordDO ->
                commentHelper.buildCommentModel(commentRecordDO))
                .collect(Collectors.toList());
    }

    public Long countBySubjectId(String subjectId, Long publishTime) {
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notNull(publishTime, () -> "publishTime null");
        return commentRecordMapper.countBySubjectId(subjectId, publishTime);
    }

    public Long countUserIdBySubjectId(String subjectId, Long publishTime) {
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notNull(publishTime, () -> "publishTime null");
        return commentRecordMapper.countUserIdBySubjectId(subjectId, publishTime);
    }

    public Long countBySubjectIdAndChatGroupTopicId(String subjectId, String chatGroupTopicId, Long publishTime) {
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notBlank(chatGroupTopicId, () -> "chatGroupTopicId empty");
        AssertUtil.notNull(publishTime, () -> "publishTime null");
        return commentRecordMapper.countBySubjectIdAndChatGroupTopicId(subjectId, chatGroupTopicId, publishTime);
    }

    public Long countUserBySubjectIdAndChatGroupTopicId(String subjectId, String chatGroupTopicId, Long publishTime) {
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notBlank(chatGroupTopicId, () -> "chatGroupTopicId empty");
        AssertUtil.notNull(publishTime, () -> "publishTime null");
        return commentRecordMapper.countUserBySubjectIdAndChatGroupTopicId(subjectId, chatGroupTopicId, publishTime);
    }

    private void checkPublishCommentRight(Long userId, SubjectModel subjectModel, String parentCommentId, Long clientId) throws CommonBizException {
        if (StringUtils.isNotBlank(parentCommentId)) {
            CommentKey commentKey = new CommentKey(subjectModel.getSubjectId(), parentCommentId);
            CommentModel commentModel = commentInfo(commentKey);
            AssertUtil.notNull(commentModel, () -> "父评论不存在" + commentKey);
            if (!commentModel.canSee(userId)) {
                throw new CommonSystemException(ErrorCode.CAN_NOT_PUBLISH_THE_COMMENT.getCode(), ErrorCode.CAN_NOT_PUBLISH_THE_COMMENT.getMsg())
                        .putExtra("commentId", parentCommentId).putExtra("userId", userId)
                        .putExtra("subjectModel", subjectModel);
            }
        }

        //熔断封禁
//        if (configClient.fuseProhibition(subjectModel.getOutBizType())) {
//            return;
//        }
//        AssertUtil.notNull(clientId, () -> "clientId null");
//        ProhibitionResult prohibitionResult = prohibitionClient.isUserProhibition(userId, subjectModel.getOutBizType(), clientId);
//        if (!prohibitionResult.isSuccess()) {
//            log.error("封禁服务不可用 {} {}", userId, subjectModel.getOutBizKey());
//            throw new CommonSystemException(ErrorCode.PROHIBITION_SERVICE_UN_AVAILABLE.getCode(), ErrorCode.PROHIBITION_SERVICE_UN_AVAILABLE.getMsg());
//        }
//        if (prohibitionResult.isProhibition()) {
//            throw new CommonBizException(ErrorCode.USER_BE_PROHIBITION.getCode(), ErrorCode.USER_BE_PROHIBITION.getMsg())
//                    .putExtra("userId", userId).putExtra("bizScene", subjectModel.getOutBizType());
//        }
    }

    /**
     * 点亮
     *
     * @param userId
     * @param commentKey
     * @return
     */
    public void light(Long userId, CommentKey commentKey) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notNull(commentKey, () -> "commentKey null");

        String subjectId = lightSubjectService.getOrCreateSubjectId(new OutBizKey(BizTypeEnum.COMMENT.getCode(), commentKey.getKey()));
        lightService.light(userId, subjectId);

        delaySyncLightCount(commentKey);
    }

    /**
     * 取消点亮
     *
     * @param userId
     * @param commentKey
     * @return
     */
    public void cancelLight(Long userId, CommentKey commentKey) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notNull(commentKey, () -> "commentKey null");

        String subjectId = lightSubjectService.getOrCreateSubjectId(new OutBizKey(BizTypeEnum.COMMENT.getCode(), commentKey.getKey()));
        lightService.cancelLight(userId, subjectId);

        delaySyncLightCount(commentKey);
    }

    public boolean hasLight(Long userId, CommentKey commentKey) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notNull(commentKey, () -> "commentKey null");

        String subjectId = lightSubjectService.getSubjectId(new OutBizKey(BizTypeEnum.COMMENT.getCode(), commentKey.getKey()));
        if (StringUtils.isNotBlank(subjectId)) {
            return lightService.hasLight(userId, subjectId);
        }
        return false;
    }


    /**
     * 点灭
     *
     * @param userId
     * @param commentKey
     * @return
     */
    public void black(Long userId, CommentKey commentKey) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notNull(commentKey, () -> "commentKey null");

        String subjectId = blackSubjectService.getOrCreateSubjectId(new OutBizKey(BizTypeEnum.COMMENT.getCode(), commentKey.getKey()));
        blackService.black(userId, subjectId);

        delaySyncBlackCount(commentKey);
    }

    /**
     * 取消点灭
     *
     * @param userId
     * @param commentKey
     * @return
     */
    public void cancelBlack(Long userId, CommentKey commentKey) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notNull(commentKey, () -> "commentKey null");

        String subjectId = blackSubjectService.getOrCreateSubjectId(new OutBizKey(BizTypeEnum.COMMENT.getCode(), commentKey.getKey()));
        blackService.cancelBlack(userId, subjectId);

        delaySyncBlackCount(commentKey);
    }

    public boolean hasBlack(Long userId, CommentKey commentKey) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notNull(commentKey, () -> "commentKey null");

        String subjectId = blackSubjectService.getSubjectId(new OutBizKey(BizTypeEnum.COMMENT.getCode(), commentKey.getKey()));
        if (StringUtils.isNotBlank(subjectId)) {
            return blackService.hasBlack(userId, subjectId);
        }
        return false;
    }


    /**
     * 主体评论列表
     *
     * @param subjectId
     * @param commentIdList
     * @param cursor
     * @return
     */
    public List<CommentModel> commentList(String subjectId,
                                          List<String> commentIdList,
                                          CommentCursor cursor,
                                          List<String> commentStatusList) {

        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        if (CollectionUtils.isNotEmpty(commentIdList)) {
            List<CommentRecordDO> commentRecordDOList = commentRecordMapper.selectByCommentIdList(subjectId, commentIdList);
            return commentRecordDOList.stream().map(commentHelper::buildCommentModel)
                    .map(this::fillLightCount).collect(Collectors.toList());
        }

        if (Objects.nonNull(cursor)) {
            AssertUtil.notNull(cursor.getPublishTime(), () -> "commentCursor publishTime null");
            AssertUtil.notNull(cursor.getLimit(), () -> "commentCursor limit null");
            List<CommentRecordDO> commentRecordDOList = commentRecordMapper.selectByCursor(subjectId, cursor.getPublishTime(), cursor.getLimit(), commentStatusList);
            return commentRecordDOList.stream().map(commentHelper::buildCommentModel)
                    .map(this::fillLightCount).collect(Collectors.toList());
        }
        throw new RuntimeException("commentList can not arrive here");
    }

    public List<CommentModel> selectByChatGroupTopicId(String subjectId, String chatGroupTopicId, CommentCursor cursor, List<String> commentStatusList) {
        AssertUtil.notBlank(subjectId, () -> "commentKey empty");
        AssertUtil.notBlank(chatGroupTopicId, () -> "chatGroupTopicId empty");

        if (Objects.nonNull(cursor)) {
            AssertUtil.notNull(cursor.getPublishTime(), () -> "commentCursor publishTime null");
            AssertUtil.notNull(cursor.getLimit(), () -> "commentCursor limit null");
            List<CommentRecordDO> commentRecordDOList = commentRecordMapper.selectByChatGroupTopicId(subjectId, chatGroupTopicId, cursor.getPublishTime(), cursor.getLimit(), commentStatusList);
            return commentRecordDOList.stream().map(commentHelper::buildCommentModel)
                    .map(this::fillLightCount).collect(Collectors.toList());
        }
        throw new RuntimeException("commentList can not arrive here");
    }


    /**
     * cc的评论列表
     *
     * @param userId
     * @param subjectId
     * @param cursor
     * @return
     */
    public List<CommentModel> ccCommentList(Long userId, String subjectId,
                                            CommentCursor cursor) {


        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notNull(cursor, () -> "commentCursor null");
        AssertUtil.notNull(cursor.getPublishTime(), () -> "commentCursor publishTime null");
        AssertUtil.notNull(cursor.getLimit(), () -> "commentCursor limit null");

        List<CommentRecordDO> commentRecordDOList = commentRecordMapper.selectByCC(subjectId, userId, cursor.getPublishTime(), cursor.getLimit());
        return commentRecordDOList.stream().map(commentHelper::buildCommentModel)
                .map(this::fillLightCount).collect(Collectors.toList());
    }

    /**
     * 延迟同步评论点亮数
     *
     * @param commentKey
     */
    private void delaySyncLightCount(CommentKey commentKey) {
        if (lightCommentMap.get(commentKey) == null) {

            Pausable pausable = hashWheelTimer.submit(t -> {

                lightCommentMap.remove(commentKey);
                try {
                    //获取点亮数
                    Integer lightCount = getLightCount(commentKey);
                    log.info("延迟更新 评论的点亮数 commentKey:{} lightCount:{}", commentKey, lightCount);
                    commentRecordMapper.updateLightCount(commentKey.getSubjectId(), commentKey.getCommentId(), lightCount);
                } catch (Exception ex) {
                    log.error("延迟同步评论点亮数 异常 commentKey " + commentKey, ex);
                }
            }, 5, TimeUnit.SECONDS);

            lightCommentMap.put(commentKey, pausable);
        }
    }

    /**
     * 延迟同步评论点灭数
     *
     * @param commentKey
     */
    private void delaySyncBlackCount(CommentKey commentKey) {
        if (blackCommentMap.get(commentKey) == null) {

            Pausable pausable = hashWheelTimer.submit(t -> {

                blackCommentMap.remove(commentKey);
                try {
                    //获取点灭数
                    Integer blackCount = getBlackCount(commentKey);
                    log.info("延迟更新 评论的点灭数 commentKey:{} blackCount:{}", commentKey, blackCount);
                    commentRecordMapper.updateBlackCount(commentKey.getSubjectId(), commentKey.getCommentId(), blackCount);
                } catch (Exception ex) {
                    log.error("延迟同步评论点灭数 异常 commentKey " + commentKey, ex);
                }
            }, 5, TimeUnit.SECONDS);

            blackCommentMap.put(commentKey, pausable);
        }
    }


    private CommentModel fillLightCount(CommentModel commentModel) {
        commentModel.setLightCount(getLightCount(commentModel.getCommentKey()));
        return commentModel;
    }

    private CommentModel fillBlackCount(CommentModel commentModel) {
        commentModel.setBlackCount(getBlackCount(commentModel.getCommentKey()));
        return commentModel;
    }

    /**
     * 获取点亮数
     *
     * @param commentKey
     * @return
     */
    public Integer getLightCount(CommentKey commentKey) {

        AssertUtil.notNull(commentKey, () -> "commentKey null");

        return lightSubjectService.getLightCount(new OutBizKey(BizTypeEnum.COMMENT.getCode(), commentKey.getKey()));
    }

    public Integer getBlackCount(CommentKey commentKey) {

        AssertUtil.notNull(commentKey, () -> "commentKey null");

        return blackSubjectService.getBlackCount(new OutBizKey(BizTypeEnum.COMMENT.getCode(), commentKey.getKey()));
    }


    public List<CommentModel> selectCommentBySubjectId(String subjectId,
                                                       Integer startIndex,
                                                       Integer limit) {
        AssertUtil.notBlank(subjectId, () -> "subjectId null");
        AssertUtil.notNull(startIndex, () -> "startIndex null");
        AssertUtil.notNull(limit, () -> "limit null");

        List<CommentRecordDO> commentRecordDOList = commentRecordMapper.selectCommentPageBySubjectId(subjectId, startIndex, limit);

        return commentRecordDOList.stream()
                .map(commentRecordDO -> commentHelper.buildCommentModel(commentRecordDO, false))
                .collect(Collectors.toList());
    }

    public Integer countCommentBySubjectId(String subjectId) {
        return commentRecordMapper.countCommentBySubjectId(subjectId);
    }

    public Integer countCommentUserBySubjectId(String subjectId) {
        return commentRecordMapper.countCommentUserBySubjectId(subjectId);
    }


    public List<Map<String, String>> statsUV(String subjectId, List<String> contentList, Long beginTime, Long endTime) {
        List<Map<String, String>> list = commentRecordMapper.statsUV(subjectId, contentList, beginTime, endTime);
        return list;
    }

    public List<Long> getLightUserIdList(CommentKey commentKey) {

        AssertUtil.notNull(commentKey, () -> "commentKey null");

        return lightSubjectService.getLightUserIdList(new OutBizKey(BizTypeEnum.COMMENT.getCode(), commentKey.getKey()));
    }


    public List<Long> getUserIdListBySubjectId(String subjectId){
        return commentRecordMapper.selectUserIdBySubjectId(subjectId);
    }

    public List<Long> getUserIdListBySubjectIdAndPublishTime(String subjectId,Long publishTime){
        return commentRecordMapper.selectUserIdBySubjectIdAndPublishTime(subjectId, publishTime);
    }




}
