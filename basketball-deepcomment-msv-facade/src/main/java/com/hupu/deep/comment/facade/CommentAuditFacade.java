package com.hupu.deep.comment.facade;

import com.hupu.deep.BaseRequest;
import com.hupu.deep.comment.request.CommentAuditRequest;
import com.hupu.foundation.result.SimpleResult;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "basketball-deepcomment-msv")
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface CommentAuditFacade {

    @PostMapping("/bpl/comment/audit")
    SimpleResult<String> auditComment(@RequestBody CommentAuditRequest request);

    @PostMapping("/bpl/comment/delete")
    SimpleResult<String> deleteComment(@RequestBody BaseRequest request);
}
