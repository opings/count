package com.hupu.deep.comment.helper;

import com.hupu.deep.comment.entity.CommentSubjectDO;
import com.hupu.deep.comment.model.subject.CommentSubjectModel;
import com.hupu.deep.comment.types.OutBizKey;
import org.springframework.stereotype.Component;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 23:38:00
 */
@Component
public class SubjectHelper {

    public CommentSubjectModel buildCommentSubjectModel(CommentSubjectDO commentSubjectDO) {

        if (null == commentSubjectDO) {
            return null;
        }
        CommentSubjectModel commentSubjectModel = new CommentSubjectModel();
        commentSubjectModel.setSubjectId(commentSubjectDO.getSubjectId());
        commentSubjectModel.setOutBizKey(new OutBizKey(commentSubjectDO.getOutBizType(), commentSubjectDO.getOutBizNo()));
        commentSubjectModel.setCommentCount(commentSubjectDO.getCommentCount());
        return commentSubjectModel;
    }
}
