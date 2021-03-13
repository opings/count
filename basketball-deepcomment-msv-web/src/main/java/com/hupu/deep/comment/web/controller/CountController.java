package com.hupu.deep.comment.web.controller;

import com.hupu.deep.comment.entity.CountRecordDO;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.count.facade.CountFacade;
import com.hupu.deep.count.request.CountRequest;
import com.hupu.deep.count.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jiangfangyuan
 * @since 2020-07-09 21:45
 */
@RestController
public class CountController implements CountFacade {

    @Autowired
    private CountService countService;

    @Override
    public Integer count(@RequestBody CountRequest request) {
        return countService.publishOrUpdateCountFlow(request.getOutBizKey(), request.getCountValue(), request.getIdempotentNo());
    }

    @Override
    public Integer queryCountValue(@RequestBody OutBizKey outBizKey) {
        return countService.queryCountValue(outBizKey);
    }

    @Override
    public Integer getCountValue(@RequestParam("outBizType") String outBizType, @RequestParam("outBizNo") String outBizNo) {
        return countService.queryCountValue(new OutBizKey(outBizType, outBizNo));
    }


    @GetMapping("/bpl/count/recordList")
    public List<CountRecordDO> countRecordList(@RequestParam("outBizType") String outBizType,
                                               @RequestParam("outBizNo") String outBizNo) {
        return countService.countRecordList(new OutBizKey(outBizType, outBizNo));
    }
}
