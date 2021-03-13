package com.hupu.deep.score.facade;


import com.hupu.deep.comment.model.ScoreRecordModel;
import com.hupu.deep.comment.model.subject.ScoreSubjectModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.types.ScoreKey;
import com.hupu.deep.score.request.MultipleUserSubjectScoreRecordListByOutKeyRequest;
import com.hupu.deep.score.request.ScoreRequest;
import com.hupu.deep.score.request.SubjectScoreRecordByOutKeyRequest;
import com.hupu.deep.score.request.SingleUserSubjectScoreRecordListByOutKeyRequest;
import com.hupu.foundation.result.SimpleResult;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @Author yuepenglei
 * @Description 打分逻辑API
 * @Date 2020/03/13 13:39
 **/
@FeignClient(name = "basketball-deepcomment-msv")
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface ScoreFacade {

    /**
     * 打分
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/comment/score")
    SimpleResult<ScoreKey> saveScore(@RequestBody ScoreRequest request);

    /**
     * 获取主体打分汇总
     *
     * @param outBizKey
     * @return
     */
    @GetMapping("/deep/comment/score/subject")
    SimpleResult<ScoreSubjectModel> subjectInfo(@RequestBody OutBizKey outBizKey);


    /**
     * 获取用户对主体的打分记录
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/score/subject/record")
    SimpleResult<ScoreRecordModel> getSubjectScoreRecord(@RequestBody SubjectScoreRecordByOutKeyRequest request);

    /**
     * 获取用户对主体的打分记录
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/singleUser/score/subject/recordList")
    SimpleResult<List<ScoreRecordModel>> getSingleUserScoreRecordList(@RequestBody SingleUserSubjectScoreRecordListByOutKeyRequest request) ;

    /**
     * 获取用户对主体的打分记录
     * @param request
     * @return
     */
    @GetMapping("/deep/comment/multipleUser/score/subject/recordList")
    SimpleResult<List<ScoreRecordModel>> getMultipleUserScoreRecordList(@RequestBody MultipleUserSubjectScoreRecordListByOutKeyRequest request);
}
