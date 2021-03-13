package com.hupu.deep.comment.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.facade.CommentSubjectFacade;
import com.hupu.deep.comment.model.subject.SubjectModel;
import com.hupu.deep.comment.request.CreateCommentSubjectRequest;
import com.hupu.deep.comment.request.QueryCommentSubjectRequest;
import com.hupu.deep.comment.service.CommentSubjectService;
import com.hupu.deep.comment.util.CacheConfig;
import com.hupu.deep.comment.util.Globals;
import com.hupu.foundation.cache.CacheClient;
import com.hupu.foundation.result.SimpleResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author zhuwenkang
 * @Time 2020年03月11日 13:53:00
 */
@RestController
@Api("主体服务")
public class CommentSubjectController implements CommentSubjectFacade {

    @Autowired
    private CommentSubjectService commentSubjectService;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private ConfigClient configClient;


    @Override
    public SimpleResult<String> createSubject(@RequestBody CreateCommentSubjectRequest request) {
        String subjectId = commentSubjectService.createSubject(request.getOutBizKey());
        return SimpleResult.success(subjectId);
    }

    @Override
    public SimpleResult<String> getSubjectId(@RequestBody QueryCommentSubjectRequest request) {
        String subjectId = commentSubjectService.getSubjectId(request.getOutBizKey());
        return SimpleResult.success(subjectId);
    }

    @Override
    public SimpleResult<SubjectModel> getSubjectInfo(@RequestParam("subjectId")String subjectId) {
        return SimpleResult.success(commentSubjectService.subjectInfo(subjectId));
    }

//    @Override
//    public SimpleResult<Map<String, String>> subjectTypeLit() {
//
//        List<String> outBizTypeList = cacheClient.get(CacheConfig.COMMENT, "outBizTypeList", () ->
//                Lists.newArrayList(commentSubjectService.outBizTypeList()), Globals.NUM_60);
//
//        Map<String, String> resultMap = Maps.newHashMap();
//        outBizTypeList.stream().forEach(item -> {
//            resultMap.put(item, configClient.getOutBizTypeMap().get(item));
//        });
//        return SimpleResult.success(resultMap);
//    }
}
