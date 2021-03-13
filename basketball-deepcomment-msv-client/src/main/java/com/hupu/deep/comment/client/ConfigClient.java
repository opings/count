package com.hupu.deep.comment.client;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.hupu.foundation.util.LogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 11:33
 */
@Service
@Slf4j
public class ConfigClient implements InitializingBean {


    /**
     * 信息治理 人工审核topic
     */
    @Getter
    @Value("${ons.manualAuditTopic}")
    private String manualAuditTopic;


    /**
     * 信息治理 人工审核 eventCode
     */
    @Getter
    @Value("${ons.manualAuditEventCode}")
    private String manualAuditEventCode;

    /**
     * 点亮 topic
     */
    @Getter
    @Value("${ons.lightTopic}")
    private String lightTopic;

    /**
     * 点灭 topic
     */
    @Getter
    @Value("${ons.blackTopic}")
    private String blackTopic;

    /**
     * 评论 topic
     */
    @Getter
    @Value("${ons.commentTopic}")
    private String commentTopic;

    /**
     * 打分 topic
     */
    @Getter
    @Value("${ons.scoreTopic}")
    private String scoreTopic;

    @Override
    public void afterPropertiesSet() throws Exception {

        com.ctrip.framework.apollo.ConfigService.getAppConfig().addChangeListener(changeEvent -> {
            for (String key : changeEvent.changedKeys()) {
                ConfigChange change = changeEvent.getChange(key);
                LogUtil.info(log, "changeKey {} changeValue {}", change.getPropertyName(), change.getNewValue());


                if ("manualAuditTopic".equalsIgnoreCase(change.getPropertyName())
                        && StringUtils.isNotBlank(change.getNewValue())) {
                    this.manualAuditTopic = change.getNewValue();
                }
                if ("manualAuditEventCode".equalsIgnoreCase(change.getPropertyName())
                        && StringUtils.isNotBlank(change.getNewValue())) {
                    this.manualAuditEventCode = change.getNewValue();
                }
                if ("lightTopic".equalsIgnoreCase(change.getPropertyName())
                        && StringUtils.isNotBlank(change.getNewValue())) {
                    this.lightTopic = change.getNewValue();
                }
                if ("commentTopic".equalsIgnoreCase(change.getPropertyName())
                        && StringUtils.isNotBlank(change.getNewValue())) {
                    this.commentTopic = change.getNewValue();
                }
                if ("scoreTopic".equalsIgnoreCase(change.getPropertyName())
                        && StringUtils.isNotBlank(change.getNewValue())) {
                    this.scoreTopic = change.getNewValue();
                }

            }
        });
    }

}
