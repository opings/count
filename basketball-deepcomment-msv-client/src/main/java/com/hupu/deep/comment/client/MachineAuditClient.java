package com.hupu.deep.comment.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hupu.deep.comment.client.dto.MachineAuditResult;
import com.hupu.deep.comment.client.feign.FeignMachineAuditClient;
import com.hupu.deep.comment.client.request.MachineAuditReq;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * 机器审核
 *
 * @author jiangfangyuan
 * @since 2020-03-13 14:07
 */
@Service
@Slf4j
public class MachineAuditClient {


    @Autowired
    private FeignMachineAuditClient feignMachineAuditClient;

    @Autowired
    private ConfigClient configClient;

    public MachineAuditResult machineAudit(String appId, String content) {

        AssertUtil.notBlank(appId, () -> "appId empty");
        AssertUtil.notBlank(content, () -> "content empty");
        
        try {
            log.info("开始发起机器审核 {} {}", appId, content);
            MachineAuditReq machineAuditReq = new MachineAuditReq();
            machineAuditReq.setContent(content);
            String result = feignMachineAuditClient.machineAudit(appId, machineAuditReq);
            log.info("结束发起机器审核 {}", result);

            if (StringUtils.isNotBlank(result)) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getInteger("code") == HttpStatus.OK.value()) {
                    String suggestion = jsonObject.getString("suggestion");
                    String requestId = jsonObject.getString("request_id");

                    MachineAuditResult machineAuditResult = new MachineAuditResult();
                    machineAuditResult.setSuccess(true);
                    machineAuditResult.setRequestId(requestId);
                    machineAuditResult.setPass(StringUtils.equalsIgnoreCase(suggestion, "pass"));
                    return machineAuditResult;
                }
            }
        } catch (Exception ex) {
            log.error("机器审核 系统异常 appId " + appId + "content" + content, ex);
        }
        MachineAuditResult machineAuditResult = new MachineAuditResult();
        machineAuditResult.setSuccess(true);
        return machineAuditResult;
    }



}
