package com.hupu.deep.count.facade;

import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.count.request.CountRequest;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 计数服务
 *
 * @author jiangfangyuan
 * @since 2020-03-15 18:57
 */
@FeignClient(name = "basketball-deepcomment-msv")
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface CountFacade {

    /**
     * 计数
     *
     * @param countRequest
     * @return
     */
    @PostMapping("/deep/count/count")
    Integer count(@RequestBody CountRequest countRequest);


    /**
     * 查询计数值
     *
     * @param outBizKey
     * @return
     */
    @GetMapping("/deep/count/query")
    Integer queryCountValue(@RequestBody OutBizKey outBizKey);


    /**
     * 查询计数值
     *
     * @param outBizType
     * @param outBizNo
     * @return
     */
    @GetMapping("/deep/count/get")
    Integer getCountValue(@RequestParam("outBizType") String outBizType, @RequestParam("outBizNo") String outBizNo);


}
