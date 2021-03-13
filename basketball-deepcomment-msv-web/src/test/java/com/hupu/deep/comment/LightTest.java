package com.hupu.deep.comment;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.google.common.collect.Maps;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.comment.web.controller.LightController;
import com.hupu.deep.light.request.LightRequest;
import com.hupu.deep.light.service.LightSubjectService;
import com.hupu.deep.mqnotify.mqhander.PostLightEventHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 16:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeepCommentApplication.class)
public class LightTest extends BaseTest {

    @Autowired
    private LightController lightController;


    @Autowired
    private PostLightEventHandler postLightEventHandler;

    @Autowired
    private LightSubjectService lightSubjectService;


    @Test
    public void light() {

        Long userId = 12342424L;
        OutBizKey outBizKey = new OutBizKey("comment", "11231");

        LightRequest lightRequest = new LightRequest();
        lightRequest.setUserId(userId);
        lightRequest.setOutBizKey(outBizKey);

        lightController.light(lightRequest);
        postLightEventHandler.handleMsg(buildMessage(userId, lightSubjectService.getSubjectId(outBizKey)));
        Integer count1 = lightController.subjectInfo(lightRequest).getResult().getLightCount();

        String lightResult = lightController.hasLight(lightRequest).getResult();
        Assert.assertEquals(lightResult, Globals.Y);


        lightController.cancelLight(lightRequest);
        lightResult = lightController.hasLight(lightRequest).getResult();
        Assert.assertEquals(lightResult, Globals.N);
        postLightEventHandler.handleMsg(buildMessage(userId, lightSubjectService.getSubjectId(outBizKey)));

        Integer count2 = lightController.subjectInfo(lightRequest).getResult().getLightCount();
        Assert.assertTrue(count1 == count2 + 1);


        lightController.light(lightRequest);
        postLightEventHandler.handleMsg(buildMessage(userId, lightSubjectService.getSubjectId(outBizKey)));


        Integer count3 = lightController.subjectInfo(lightRequest).getResult().getLightCount();
        Assert.assertTrue(count3 == count2 + 1);
    }


    private Message buildMessage(Long userId, String subjectId) {
        Map<String, Object> msgBodyMap = Maps.newHashMap();
        msgBodyMap.put("userId", userId);
        msgBodyMap.put("subjectId", subjectId);
        Message message = new Message();
        message.setBody(JSON.toJSONString(msgBodyMap).getBytes());
        return message;
    }


}
