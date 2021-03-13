package com.hupu.deep.mqnotify;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.google.common.collect.Maps;
import com.hupu.deep.comment.util.MqEventCode;
import com.hupu.deep.mqnotify.mqhander.EventHandler;
import com.hupu.deep.mqnotify.mqhander.PostBlackEventHandler;
import com.hupu.deep.mqnotify.mqhander.PostLightEventHandler;
import com.hupu.deep.mqnotify.mqhander.comment.CommentAuditEventHandler;
import com.hupu.deep.mqnotify.mqhander.comment.PostCommentEventHandler;
import com.hupu.deep.mqnotify.mqhander.PostScoreEventHandler;
import com.hupu.foundation.error.CommonSystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 15:05
 */
@Service
@Slf4j
public class MqListener implements MessageListener, InitializingBean {


    private Map<String, EventHandler> eventHandlerMap = Maps.newConcurrentMap();


    @Autowired
    private PostLightEventHandler postLightEventHandler;

    @Autowired
    private PostBlackEventHandler postBlackEventHandler;

    @Autowired
    private PostCommentEventHandler postCommentEventHandler;

    @Autowired
    private CommentAuditEventHandler commentAuditEventHandler;

    @Autowired
    private PostScoreEventHandler postScoreEventHandler;

    @Override
    public Action consume(Message message, ConsumeContext context) {


        ThreadContext.put("traceId", UUID.randomUUID().toString());

        String eventCode = message.getTag();
        log.info("接收到消息 {} {}", eventCode, message);
        try {
            return Optional.ofNullable(eventHandlerMap.get(eventCode))
                    .map(eventHandler -> eventHandler.handleMsg(message))
                    .orElseGet(() -> {
                        log.error("不支持的eventCode：" + eventCode);
                        return Action.CommitMessage;
                    });

        } catch (Exception ex) {
            log.error("消息处理异常 eventCode:" + eventCode + " msgBody:" + new String(message.getBody()) + " message:" + message, ex);
            throw new CommonSystemException("消息处理异常", "消息处理异常");
        } finally {
            ThreadContext.remove("traceId");
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventHandlerMap.put(MqEventCode.EC_LIGHT, postLightEventHandler);
        eventHandlerMap.put(MqEventCode.EC_CANCEL_LIGHT, postLightEventHandler);

        eventHandlerMap.put(MqEventCode.EC_BLACK, postBlackEventHandler);
        eventHandlerMap.put(MqEventCode.EC_CANCEL_BLACK, postBlackEventHandler);


        eventHandlerMap.put(MqEventCode.EC_PUBLISH_COMMENT, postCommentEventHandler);
        eventHandlerMap.put(MqEventCode.EC_AUDIT_COMMENT, commentAuditEventHandler);

        eventHandlerMap.put(MqEventCode.EC_SCORE, postScoreEventHandler);
    }
}
