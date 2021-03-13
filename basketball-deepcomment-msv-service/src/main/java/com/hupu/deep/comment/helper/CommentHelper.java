package com.hupu.deep.comment.helper;

import com.hupu.deep.comment.dal.CommentRecordMapper;
import com.hupu.deep.comment.entity.CommentRecordDO;
import com.hupu.deep.comment.enums.AuditStatusEnum;
import com.hupu.deep.comment.model.CommentModel;
import com.hupu.deep.comment.types.CommentKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 23:50:00
 */
@Component
@Slf4j
public class CommentHelper {


    @Autowired
    private CommentRecordMapper commentRecordMapper;

    /**
     * 构建一条评论状态置为待审核
     *
     * @param commentKey      commentKey
     * @param userId          userId
     * @param parentCommentId parentCommentId
     * @param content         content
     * @return CommentRecordDO
     */
    public CommentRecordDO buildCommentRecordDO(CommentKey commentKey, String parentCommentId,
                                                String chatGroupTopicId, String rongcloudMsgId,Long rongcloudMsgTime,
                                                Long userId,
                                                String content, String contentExtend) {
        CommentRecordDO commentRecordDO = new CommentRecordDO();

        commentRecordDO.setUserId(userId);
        commentRecordDO.setChatGroupTopicId(chatGroupTopicId);
        commentRecordDO.setRongcloudMsgId(rongcloudMsgId);
        commentRecordDO.setRongcloudMsgTime(rongcloudMsgTime);
        commentRecordDO.setSubjectId(commentKey.getSubjectId());
        commentRecordDO.setCommentId(commentKey.getCommentId());
        commentRecordDO.setParentCommentId(parentCommentId);
        commentRecordDO.setCommentContent(content);
        commentRecordDO.setCommentContentExtend(contentExtend);
        commentRecordDO.setFinalAuditStatus(AuditStatusEnum.WAITING_AUDIT.getCode());
        return commentRecordDO;
    }

    public CommentModel buildCommentModel(CommentRecordDO commentRecordDO) {
        return buildCommentModel(commentRecordDO, true);
    }

    public CommentModel buildCommentModel(CommentRecordDO commentRecordDO, boolean fillParent) {

        if (null == commentRecordDO) {
            return null;
        }
        CommentModel commentModel = new CommentModel();
        commentModel.setUserId(commentRecordDO.getUserId());
        commentModel.setRongcloudMsgId(commentRecordDO.getRongcloudMsgId());
        commentModel.setRongcloudMsgTime(commentRecordDO.getRongcloudMsgTime());
        commentModel.setRongcloudRoomMsgId(commentRecordDO.getRongcloudRoomMsgId());
        commentModel.setRongcloudRoomMsgTime(commentRecordDO.getRongcloudRoomMsgTime());
        commentModel.setChatGroupTopicId(commentRecordDO.getChatGroupTopicId());
        commentModel.setCommentKey(new CommentKey(commentRecordDO.getSubjectId(), commentRecordDO.getCommentId()));
        commentModel.setCommentContent(commentRecordDO.getCommentContent());
        commentModel.setMachineAuditStatus(AuditStatusEnum.getByCode(commentRecordDO.getMachineAuditStatus()));
        commentModel.setManualAuditStatus(AuditStatusEnum.getByCode(commentRecordDO.getManualAuditStatus()));
        commentModel.setFinalAuditStatus(AuditStatusEnum.getByCode(commentRecordDO.getFinalAuditStatus()));
        commentModel.setReplyCount(commentRecordDO.getReplyCount());
        commentModel.setPublishTime(commentRecordDO.getPublishTime());

        commentModel.setLightCount(commentRecordDO.getLightCount());
        commentModel.setBlackCount(commentRecordDO.getBlackCount());

        if (fillParent) {
            if (StringUtils.isNotBlank(commentRecordDO.getParentCommentId())) {
                commentModel.setParentCommentModel(buildCommentModel(commentRecordMapper.selectByCommentId(commentRecordDO.getSubjectId(), commentRecordDO.getParentCommentId()),
                        Boolean.FALSE));
            }
        }
        commentModel.setCommentContentExtend(commentRecordDO.getCommentContentExtend());
        commentModel.setAuditUserName(commentRecordDO.getAuditUserName());
        return commentModel;
    }
}
