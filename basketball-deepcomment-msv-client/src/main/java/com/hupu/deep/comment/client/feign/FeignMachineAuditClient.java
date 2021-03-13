package com.hupu.deep.comment.client.feign;

import com.hupu.deep.comment.client.request.MachineAuditReq;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 15:23
 */
@FeignClient(name = "rcp-contentreview-msv")
@Headers({"Content-Type: application/json;charset=UTF-8"})
public interface FeignMachineAuditClient {

    /**
     * 机器审核
     * @param appId
     * @param machineAuditReq
     * @return
     */
    @PostMapping(value = "/rcp/content/check")
    String machineAudit(@RequestHeader(value = "appId", required = false) String appId,
                        @RequestBody MachineAuditReq machineAuditReq);
}
