package com.hupu.deep.light.facade;

import com.hupu.deep.comment.model.subject.LightSubjectModel;
import com.hupu.deep.light.request.LightRequest;
import com.hupu.foundation.result.SimpleResult;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 20:55
 */
@FeignClient(name = "basketball-deepcomment-msv")
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface LightFacade {


    /**
     * 点亮
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/light/light")
    SimpleResult<String> light(@RequestBody LightRequest request);

    /**
     * 取消点亮
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/light/cancelLight")
    SimpleResult<String> cancelLight(@RequestBody LightRequest request);

    /**
     * 是否点亮
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/light/hasLight")
    SimpleResult<String> hasLight(@RequestBody LightRequest request);


    /**
     * 查询点亮主体
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/light/lightSubject")
    SimpleResult<LightSubjectModel> subjectInfo(@RequestBody LightRequest request);


}
