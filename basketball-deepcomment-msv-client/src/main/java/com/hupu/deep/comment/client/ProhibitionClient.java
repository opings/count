package com.hupu.deep.comment.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hupu.deep.comment.client.dto.ProhibitionResult;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 17:27
 */
@Service
@Slf4j
public class ProhibitionClient {


    @Autowired
    private ConfigClient configClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 判断用户是否封禁
     *
     * @param userId
     * @param bizScene
     * @return
     */
    public ProhibitionResult isUserProhibition(Long userId, String bizScene, Long clientId) {

        AssertUtil.notNull(userId, () -> "userId null");
        AssertUtil.notBlank(bizScene, () -> "bizScene empty");
        AssertUtil.notNull(clientId, () -> "clientId null");
//
//        try {
//            String prohibitionUrl = configClient.getUrlConfig().getProhibitionUrl();
//
//            Map<String, String> requestBody = Maps.newHashMap();
//            requestBody.put("cid", clientId.toString());
//            requestBody.put("uid", userId.toString());
//            requestBody.put("range", bizScene);
//
//            String result = restTemplate.postForObject(prohibitionUrl, requestBody, String.class);
//
//            log.info("isUserProhibition {} {} ", JSON.toJSONString(requestBody), result);
//
//            if (StringUtils.isNotBlank(result)) {
//                JSONObject jsonObject = JSON.parseObject(result);
//                ProhibitionResult prohibitionResult = new ProhibitionResult();
//                prohibitionResult.setSuccess(true);
//                prohibitionResult.setProhibition(Objects.equals(Boolean.FALSE, Boolean.valueOf(jsonObject.getString("pass"))));
//                return prohibitionResult;
//            }
//        } catch (Exception ex) {
//            log.error("封禁服务系统异常 userId " + userId + " bizScene" + bizScene, ex);
//        }
        ProhibitionResult prohibitionResult = new ProhibitionResult();
        prohibitionResult.setSuccess(false);
        return prohibitionResult;
    }

    //只检查 userId
    public ProhibitionResult isUserProhibition(Long userId, String bizScene)
    {
        return isUserProhibition(userId,bizScene,0L);
    }
}
