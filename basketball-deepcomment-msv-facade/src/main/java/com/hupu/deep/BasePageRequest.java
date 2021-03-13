package com.hupu.deep;

import lombok.Data;

/**
 * @author jiangfangyuan
 * @since 2020-03-22 14:22
 */
@Data
public class BasePageRequest extends BaseRequest {

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer pageNumber;
}
