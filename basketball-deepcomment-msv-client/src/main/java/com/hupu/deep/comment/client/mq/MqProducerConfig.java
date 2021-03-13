package com.hupu.deep.comment.client.mq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 13:57
 */
@Configuration
public class MqProducerConfig {

    @Value("${ons.accessKey}")
    private String accessKey;

    @Value("${ons.secretKey}")
    private String secretKey;

    @Value("${ons.nameSrvAddr}")
    private String nameSrvAddr;


    @Value("${ons.manualAuditNameSrvAddr}")
    private String manualAuditNameSrvAddr;


    @Autowired
    private SimpleTransactionChecker simpleTransactionChecker;

    @Bean(initMethod = "start", destroyMethod = "shutdown", name = "producerBean")
    public ProducerBean buildProducer() {
        ProducerBean producer = new ProducerBean();
        producer.setProperties(buildProducerProperties());
        return producer;
    }


    @Bean(initMethod = "start", destroyMethod = "shutdown", name = "manualAuditProducerBean")
    public ProducerBean manualAuditProducerBean() {
        ProducerBean producer = new ProducerBean();
        producer.setProperties(buildManualAuditProducerProperties());
        return producer;
    }


    @Bean(initMethod = "start", destroyMethod = "shutdown", name = "transactionProducerBean")
    public TransactionProducerBean buildTransactionProducer() {
        TransactionProducerBean producer = new TransactionProducerBean();
        producer.setProperties(buildProducerProperties());
        producer.setLocalTransactionChecker(simpleTransactionChecker);
        return producer;
    }

    private Properties buildProducerProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        return properties;
    }

    private Properties buildManualAuditProducerProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.manualAuditNameSrvAddr);
        return properties;
    }


}
