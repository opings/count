package com.hupu.deep.comment.web.controller;

import com.hupu.deep.comment.model.ScoreRecordModel;
import com.hupu.deep.comment.model.subject.ScoreSubjectModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.types.ScoreKey;
import com.hupu.deep.score.facade.ScoreFacade;
import com.hupu.deep.score.request.MultipleUserSubjectScoreRecordListByOutKeyRequest;
import com.hupu.deep.score.request.ScoreRequest;
import com.hupu.deep.score.request.SubjectScoreRecordByOutKeyRequest;
import com.hupu.deep.score.request.SingleUserSubjectScoreRecordListByOutKeyRequest;
import com.hupu.deep.score.service.ScoreService;
import com.hupu.deep.score.service.ScoreSubjectService;
import com.hupu.foundation.result.SimpleResult;
import com.hupu.foundation.util.AssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author yuepenglei
 * @Description 打分逻辑API
 * @Date 2020/03/13 13:39
 */
@RestController
@Api("主体服务")
public class ScoreController implements ScoreFacade {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ScoreSubjectService scoreSubjectService;

    @Override
    @ApiOperation(value = "打分", httpMethod = "POST")
    public SimpleResult<ScoreKey> saveScore(@RequestBody ScoreRequest request) {
        AssertUtil.notNull(request.getUserId(), () -> "userId null");
        AssertUtil.notNull(request.getOutBizKey(), () -> "outBizKey null");
        ScoreKey scoreKey = scoreService.saveScore(scoreSubjectService.getOrCreateSubjectId(request.getOutBizKey()), request.getUserId(), request.getScore());
        return SimpleResult.success(scoreKey);
    }

    @Override
    @ApiOperation(value = "主体得分汇总", httpMethod = "GET")
    public SimpleResult<ScoreSubjectModel> subjectInfo(@RequestBody OutBizKey outBizKey) {
        AssertUtil.notNull(outBizKey, () -> "outBizKey null");
        ScoreSubjectModel scoreSubjectModel = scoreSubjectService.getScoreSubject(outBizKey);
        return SimpleResult.success(scoreSubjectModel);
    }

    @Override
    @ApiOperation(value = "获取用户对主体的打分记录", httpMethod = "GET")
    public SimpleResult<ScoreRecordModel> getSubjectScoreRecord(@RequestBody SubjectScoreRecordByOutKeyRequest request) {
        AssertUtil.notNull(request.getUserId(), () -> "userId null");
        return SimpleResult.success(scoreSubjectService.getScoreRecord(request.getUserId(), request.getOutBizKey()));
    }

    @Override
    @ApiOperation(value = "获取用户对主体的打分记录", httpMethod = "GET")
    public SimpleResult<List<ScoreRecordModel>> getSingleUserScoreRecordList(@RequestBody SingleUserSubjectScoreRecordListByOutKeyRequest request) {
        AssertUtil.notNull(request.getUserId(), () -> "userId null");
        AssertUtil.notNull(request.getOutBizKeyList(), () -> "outBizKeyList null");
        return SimpleResult.success(scoreSubjectService.getSingleUserScoreRecordList(request.getUserId(), request.getOutBizKeyList()));
    }

    @Override
    @ApiOperation(value = "获取用户对主体的打分记录", httpMethod = "GET")
    public SimpleResult<List<ScoreRecordModel>> getMultipleUserScoreRecordList(@RequestBody MultipleUserSubjectScoreRecordListByOutKeyRequest request) {
        AssertUtil.notNull(request.getUserIds(), () -> "userIds null");
        AssertUtil.notNull(request.getOutBizKey(), () -> "outBizKey null");
        return SimpleResult.success(scoreSubjectService.getMultipleUserScoreRecordList(request.getUserIds(), request.getOutBizKey()));
    }
}
