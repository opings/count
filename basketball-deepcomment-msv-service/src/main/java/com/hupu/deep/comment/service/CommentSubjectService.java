package com.hupu.deep.comment.service;

import com.hupu.deep.comment.dal.CommentSubjectMapper;
import com.hupu.deep.comment.entity.CommentSubjectDO;
import com.hupu.deep.comment.enums.BizTypeEnum;
import com.hupu.deep.comment.helper.SubjectHelper;
import com.hupu.deep.comment.model.subject.SubjectModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.CacheConfig;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.foundation.cache.CacheClient;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author zhuwenkang
 * @Time 2020年03月11日 14:32:00
 */
@Service
@Slf4j
public class CommentSubjectService {

    @Autowired
    private CommentSubjectMapper commentSubjectMapper;

    @Autowired
    private SubjectHelper subjectHelper;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private IdService idService;

    /**
     * 创建评论主体
     *
     * @param outBizKey
     * @return
     */
    public String createSubject(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        try {
            String subjectId = idService.genId(BizTypeEnum.COMMENT.getCode()).toString();
            CommentSubjectDO commentSubjectDO = new CommentSubjectDO();
            commentSubjectDO.setSubjectId(subjectId);
            commentSubjectDO.setOutBizType(outBizKey.getOutBizType());
            commentSubjectDO.setOutBizNo(outBizKey.getOutBizNo());
            commentSubjectMapper.insertSelective(commentSubjectDO);
            return subjectId;
        } catch (DuplicateKeyException ex) {
            log.info("重复创建评论主体 " + outBizKey);
            return commentSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo())
                    .getSubjectId();
        }
    }

    /**
     * 查询评论主体
     *
     * @param outBizKey
     * @return
     */
    public String getSubjectId(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        return cacheClient.get(CacheConfig.COMMENT, outBizKey.getKey(), () -> {
            CommentSubjectDO commentSubjectDO = commentSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo());
            if (null != commentSubjectDO) {
                return commentSubjectDO.getSubjectId();
            }
            return null;
        }, Globals.NUM_60);
    }

    public SubjectModel subjectInfo(String subjectId) {

        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        return cacheClient.get("subjectInfo", subjectId, () -> {
            CommentSubjectDO commentSubjectDO = commentSubjectMapper.selectBySubjectId(subjectId);
            return subjectHelper.buildCommentSubjectModel(commentSubjectDO);
        }, Globals.NUM_60);
    }


    public SubjectModel subjectInfo(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        return cacheClient.get("subjectInfo", outBizKey.getKey(), () -> {
            CommentSubjectDO commentSubjectDO = commentSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo());
            return subjectHelper.buildCommentSubjectModel(commentSubjectDO);
        }, Globals.NUM_60);
    }


    public List<String> outBizTypeList() {
        return commentSubjectMapper.outBizTypeList();
    }
}
