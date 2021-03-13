package com.hupu.deep.mqnotify.mqhander;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.google.common.collect.Lists;
import com.hupu.foundation.util.AssertUtil;

import java.util.List;
import java.util.Objects;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 15:11
 */
public class EventHandlerChain implements EventHandler {

    private List<EventHandler> eventHandlerList;

    public EventHandlerChain(List<EventHandler> eventHandlerList) {
        AssertUtil.notNull(eventHandlerList, () -> "eventHandlerList null");
        this.eventHandlerList = eventHandlerList;
    }

    @Override
    public Action handleMsg(Message message) {

        List<Action> actionList = Lists.newLinkedList();
        for (EventHandler eventHandler : eventHandlerList) {
            Action action = eventHandler.handleMsg(message);
            AssertUtil.notNull(action, () -> "action null");
            actionList.add(action);
        }
        if (actionList.stream().allMatch(item -> Objects.equals(item, Action.CommitMessage))) {
            return Action.CommitMessage;
        }
        return Action.ReconsumeLater;
    }
}
