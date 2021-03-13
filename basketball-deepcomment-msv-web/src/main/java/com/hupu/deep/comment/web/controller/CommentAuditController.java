package com.hupu.deep.comment.web.controller;

import com.hupu.deep.BaseRequest;
import com.hupu.deep.comment.enums.AuditStatusEnum;
import com.hupu.deep.comment.facade.CommentAuditFacade;
import com.hupu.deep.comment.request.CommentAuditRequest;
import com.hupu.deep.comment.service.CommentAuditService;
import com.hupu.deep.comment.types.CommentKey;
import com.hupu.deep.comment.util.Globals;
import com.hupu.foundation.result.SimpleResult;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 评论审核 信息治理
 *
 * @author jiangfangyuan
 * @since 2020-03-13 10:30
 */
@RestController
@Slf4j
public class CommentAuditController implements CommentAuditFacade {


    @Autowired
    private CommentAuditService commentAuditService;


    @Override
    public SimpleResult<String> auditComment(@RequestBody CommentAuditRequest request) {


        log.info("信息治理 审核回调 {}", request);

        CommentKey commentKey = new CommentKey(request.getSubjectId(), request.getCommentId());
        AuditStatusEnum auditStatusEnum = AuditStatusEnum.getByCode(request.getAuditStatus());
        AssertUtil.notNull(auditStatusEnum, () -> "审核状态不合法" + request.getAuditStatus());

        if (Objects.equals(auditStatusEnum, AuditStatusEnum.AUDIT_PASS)) {
            log.info("信息治理 {} 人工审核通过", commentKey);
            commentAuditService.manualAuditPass(commentKey, request.getAuditUserName());
            return SimpleResult.success(Globals.Y);
        }
        if (Objects.equals(auditStatusEnum, AuditStatusEnum.AUDIT_NOT_PASS)) {
            log.info("信息治理 {} 人工审核不通过", commentKey);
            commentAuditService.manualAuditNotPass(commentKey, request.getAuditUserName());
            return SimpleResult.success(Globals.Y);
        }
        if (Objects.equals(auditStatusEnum, AuditStatusEnum.DELETE)) {
            log.info("信息治理 {} 人工审核删除", commentKey);
            commentAuditService.manualAuditDeleteComment(commentKey, request.getAuditUserName());
            return SimpleResult.success(Globals.Y);
        }
        throw new RuntimeException("auditComment can not arrive here " + request);
    }


    @Override
    public SimpleResult<String> deleteComment(@RequestBody BaseRequest request) {
        commentAuditService.manualAuditDeleteComment(request.getUserId());
        return SimpleResult.success(Globals.Y);
    }
}
