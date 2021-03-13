package com.hupu.deep.comment.client.feign;

import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author jiangfangyuan
 * @since 2020-04-13 10:29
 */
@FeignClient(name = "bbs-internal-api")
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface FeignUserClient {

    @GetMapping("/bbsinternalapi/user/v1/info")
    String getUserInfo(@RequestParam("puid") Long puid);

    @GetMapping("/bbsinternalapi/user/v1/listUserInfo")
    String getUserInfoList(@RequestParam("puid") List<Long> puid);

}
