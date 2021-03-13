package com.hupu.deep.black.facade;

import com.hupu.deep.comment.model.subject.BlackSubjectModel;
import com.hupu.deep.black.request.BlackRequest;
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
public interface BlackFacade {


    /**
     * 点灭
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/black/black")
    SimpleResult<String> black(@RequestBody BlackRequest request);

    /**
     * 取消点灭
     *
     * @param request
     * @return
     */
    @PostMapping("/deep/black/cancelBlack")
    SimpleResult<String> cancelBlack(@RequestBody BlackRequest request);

    /**
     * 是否点灭
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/black/hasBlack")
    SimpleResult<String> hasBlack(@RequestBody BlackRequest request);


    /**
     * 查询点灭主体
     *
     * @param request
     * @return
     */
    @GetMapping("/deep/black/blackSubject")
    SimpleResult<BlackSubjectModel> subjectInfo(@RequestBody BlackRequest request);


}
