package com.hupu.deep.comment.client.request;

import lombok.Data;

import java.util.List;

/**
 * 机器审核请求
 *
 * @author jiangfangyuan
 * @since 2020-03-13 14:08
 */
@Data
public class MachineAuditReq {

    /**
     * 审核选择的三方平台
     */
    private List<String> platforms;

    /**
     * 审核选择的场景
     */
    private List<String> scenes;

    /**
     * 审核选择的词库
     */
    private List<String> thesaurus;

    /**
     * 待审核标题（与正文必须包含其一）
     */
    private String title;

    /**
     * 待审核正文（与标题必须包含其一）
     */
    private String content;

}
