package com.hupu.deep.comment.client.dto;

import lombok.Data;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 15:42
 */
@Data
public class MachineAuditResult {


    /**
     * 服务是否调用成功
     */
    private boolean success;

    private boolean pass;

    private String requestId;


}
