package com.hupu.deep.comment.facade;

import com.hupu.deep.comment.model.subject.SubjectModel;
import com.hupu.deep.comment.request.CreateCommentSubjectRequest;
import com.hupu.deep.comment.request.QueryCommentSubjectRequest;
import com.hupu.foundation.result.SimpleResult;
import feign.Headers;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author zhuwenkang
 * @Time 2020年03月16日 20:37:00
 */
@FeignClient(name = "basketball-deepcomment-msv")
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface CommentSubjectFacade {

    /**
     * 创建评论主体
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/comment/createSubject")
    @ApiOperation(value = "创建主体", httpMethod = "POST")
    SimpleResult<String> createSubject(@RequestBody CreateCommentSubjectRequest request);


    /**
     * 查询评论主体
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/subjectId")
    @ApiOperation(value = "查询主体", httpMethod = "GET")
    SimpleResult<String> getSubjectId(@RequestBody QueryCommentSubjectRequest request);

    /**
     * 查询评论主体
     * @param subjectId
     * @return
     */
    @GetMapping("/deep/comment/subjectInfo")
    @ApiOperation(value = "查询主体", httpMethod = "GET")
    SimpleResult<SubjectModel> getSubjectInfo(@RequestParam("subjectId")String subjectId);


//    /**
//     * 主体类型列表
//     *
//     * @return
//     */
//    @GetMapping("/comment/subjectTypeLit")
//    @ApiOperation(value = "查询主体", httpMethod = "GET")
//    SimpleResult<Map<String,String>> subjectTypeLit();


}
