package com.hupu.deep.comment.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hupu.deep.comment.client.dto.UserInfoDTO;
import com.hupu.deep.comment.client.feign.FeignUserClient;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 14:34
 */
@Service
@Slf4j
public class UserClient {

    @Autowired
    private FeignUserClient feignUserClient;

    private static final String unknownNickName = "未知姓名";


    /**
     * 获取单个用户信息
     *
     * @param userId
     * @return
     */
    public UserInfoDTO getUser(Long userId) {

        AssertUtil.notNull(userId, () -> "userId null");
        try {
            String response = feignUserClient.getUserInfo(userId);
            JSONObject item = JSON.parseObject(response).getJSONObject("data");
            return item.getLong("puid") == null ? null : jsonToDTO(item);
        } catch (Exception ex) {
            log.error("userInfo error userId " + userId, ex);
        }
        return null;
    }


    public String getNickName(Long userId) {

        UserInfoDTO userInfoDTO = getUser(userId);
        if (null == userInfoDTO || StringUtils.isBlank(userInfoDTO.getNickName())) {
            return unknownNickName;
        }
        return userInfoDTO.getNickName();
    }

    private UserInfoDTO jsonToDTO(JSONObject item) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setUserId(item.getLong("puid"));
        dto.setNickName(item.getString("username"));
        dto.setHeader(item.getString("header"));
        return dto;
    }

}
