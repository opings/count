package com.hupu.deep.comment.client.mq;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 13:50
 */
@Service
@Slf4j
public class MqClient {


    @Autowired
    private ProducerBean producerBean;


    @Autowired
    private TransactionProducerBean transactionProducerBean;


    public <T> void sendMsg(String topic, String eventCode, T msgBody) {

        AssertUtil.notBlank(topic, () -> "topic empty");
        AssertUtil.notBlank(eventCode, () -> "tag empty");
        AssertUtil.notNull(msgBody, () -> "msgBody null");

        Message message = new Message();
        message.setTopic(topic);
        message.setTag(eventCode);

        if (msgBody instanceof String) {
            message.setBody(((String) msgBody).getBytes());
        } else {
            message.setBody(JSON.toJSONString(msgBody).getBytes());
        }

        SendResult sendResult = producerBean.send(message);
        log.info("发送消息成功 {} {}", sendResult.getTopic(), sendResult.getMessageId());
    }


    public <T> void sendMsg(String topic, String eventCode, T msgBody,ProducerBean producerBean) {

        AssertUtil.notBlank(topic, () -> "topic empty");
        AssertUtil.notBlank(eventCode, () -> "tag empty");
        AssertUtil.notNull(msgBody, () -> "msgBody null");
        AssertUtil.notNull(producerBean, () -> "producerBean null");

        Message message = new Message();
        message.setTopic(topic);
        message.setTag(eventCode);

        if (msgBody instanceof String) {
            message.setBody(((String) msgBody).getBytes());
        } else {
            message.setBody(JSON.toJSONString(msgBody).getBytes());
        }

        SendResult sendResult = producerBean.send(message);
        log.info("发送消息成功 {} {}", sendResult.getTopic(), sendResult.getMessageId());
    }


    public <T> void sendTransactionMsg(String topic, String eventCode, T msgBody, LocalTransactionExecuter localTransactionExecuter
            , Object arg) {

        AssertUtil.notBlank(topic, () -> "topic empty");
        AssertUtil.notBlank(eventCode, () -> "tag empty");
        AssertUtil.notNull(msgBody, () -> "msgBody null");
        AssertUtil.notNull(localTransactionExecuter, () -> "localTransactionExecuter null");

        Message message = new Message();
        message.setTopic(topic);
        message.setTag(eventCode);

        if (msgBody instanceof String) {
            message.setBody(((String) msgBody).getBytes());
        } else {
            message.setBody(JSON.toJSONString(msgBody).getBytes());
        }

        message.putUserProperties(PropertyKeyConst.CheckImmunityTimeInSeconds, "60");
        SendResult sendResult = transactionProducerBean.send(message, localTransactionExecuter, arg);
        log.info("发送消息成功 {} {} {}", sendResult.getTopic(), eventCode, sendResult.getMessageId());
    }
}

