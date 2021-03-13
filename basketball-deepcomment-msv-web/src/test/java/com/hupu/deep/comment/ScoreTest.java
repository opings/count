package com.hupu.deep.comment;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.google.common.collect.Maps;
import com.hupu.deep.comment.model.subject.ScoreSubjectModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.web.controller.CommentController;
import com.hupu.deep.comment.web.controller.ScoreController;
import com.hupu.deep.mqnotify.mqhander.PostScoreEventHandler;
import com.hupu.deep.score.request.ScoreRequest;
import com.hupu.foundation.result.SimpleResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author yuepenglei
 * @since 2020-03-11 16:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeepCommentApplication.class)
public class ScoreTest extends BaseTest {

    @Autowired
    private ScoreController scoreController;

    @Autowired
    private PostScoreEventHandler postScoreEventHandler;

    @Autowired
    private CommentController commentController;


    // 打分
    @Test
    public void score(){
        Long userId = 12342424L;
        OutBizKey outBizKey = new OutBizKey("comment","11231");

        // 打分
        ScoreRequest request = new ScoreRequest();
        request.setUserId(userId);
        request.setOutBizKey(outBizKey);
        request.setScore(10);
        scoreController.saveScore(request);
        postScoreEventHandler.handleMsg(buildMessage(1L,"689127799398596608"));

        //SimpleResult<ScoreSubjectModel> result1 = getScoreSubject(outBizKey);

        //SimpleResult<List<ScoreRecordModel>> result2 = getScoreRecord();

        System.out.print("测试完成");
    }

    //获取主体打分汇总
    public SimpleResult<ScoreSubjectModel> getScoreSubject(OutBizKey outBizKey){
        SimpleResult<ScoreSubjectModel> result = scoreController.subjectInfo(outBizKey);
        return result;
    }


    private Message buildMessage(Long userId,String subjectId) {
        Map<String, Object> msgBodyMap = Maps.newHashMap();
        msgBodyMap.put("userId", userId);
        msgBodyMap.put("subjectId", subjectId);
        msgBodyMap.put("score", 8);
        Message message = new Message();
        message.setBody(JSON.toJSONString(msgBodyMap).getBytes());
        return message;
    }

    @Test
    public void commentForUserAndSubject(){



    }

}
