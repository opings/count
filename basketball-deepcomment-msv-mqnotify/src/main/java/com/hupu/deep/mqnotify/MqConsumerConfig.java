package com.hupu.deep.mqnotify;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.hupu.deep.comment.client.ConfigClient;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.comment.util.MqEventCode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 18:23
 */
@Configuration
public class MqConsumerConfig {

    @Value("${ons.accessKey}")
    private String accessKey;

    @Value("${ons.secretKey}")
    private String secretKey;

    @Value("${ons.nameSrvAddr}")
    private String nameSrvAddr;

    @Getter
    @Value("${ons.commentGroupId}")
    private String commentGroupId;

    @Getter
    @Value("${ons.lightGroupId}")
    private String lightGroupId;

    @Getter
    @Value("${ons.blackGroupId}")
    private String blackGroupId;

    @Getter
    @Value("${ons.scoreGroupId}")
    private String scoreGroupId;

    @Autowired
    private ConfigClient configClient;

    @Autowired
    private MqListener mqListener;


    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean commentConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = buildConsumerProperties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, commentGroupId);
        properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        properties.put(PropertyKeyConst.ConsumeThreadNums, Globals.NUM_40);
        consumerBean.setProperties(properties);

        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = Maps.newHashMap();
        Subscription subscription = new Subscription();
        subscription.setTopic(configClient.getCommentTopic());
        subscription.setExpression(Joiner.on("||").join(MqEventCode.EC_PUBLISH_COMMENT, MqEventCode.EC_AUDIT_COMMENT));
        subscriptionTable.put(subscription, mqListener);
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }


    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean lightConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = buildConsumerProperties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, lightGroupId);
        properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        consumerBean.setProperties(properties);

        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = Maps.newHashMap();
        Subscription subscription = new Subscription();
        subscription.setTopic(configClient.getLightTopic());
        subscription.setExpression(Joiner.on("||").join(MqEventCode.EC_LIGHT, MqEventCode.EC_CANCEL_LIGHT));
        subscriptionTable.put(subscription, mqListener);
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean blackConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = buildConsumerProperties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, blackGroupId);
        properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        consumerBean.setProperties(properties);

        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = Maps.newHashMap();
        Subscription subscription = new Subscription();
        subscription.setTopic(configClient.getBlackTopic());
        subscription.setExpression(Joiner.on("||").join(MqEventCode.EC_BLACK, MqEventCode.EC_CANCEL_BLACK));
        subscriptionTable.put(subscription, mqListener);
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }


    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean scoreConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = buildConsumerProperties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, scoreGroupId);
        properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        consumerBean.setProperties(properties);

        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = Maps.newHashMap();
        Subscription subscription = new Subscription();
        subscription.setTopic(configClient.getScoreTopic());
        subscription.setExpression(MqEventCode.EC_SCORE);
        subscriptionTable.put(subscription, mqListener);
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }


    private Properties buildConsumerProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        return properties;
    }
}
