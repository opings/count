package com.hupu.deep.mqnotify.mqhander;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 15:09
 */
public interface EventHandler {


    /**
     * 处理消息
     *
     * @param message
     */
    Action handleMsg(Message message);
}
