package com.hupu.deep.count.request;

import com.hupu.deep.comment.types.OutBizKey;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangfangyuan
 * @since 2020-03-18 14:00
 */
@Data
public class CountRequest implements Serializable {

    private static final long serialVersionUID = -2373437937541852383L;

    /**
     * 外部业务单据号
     */
    private OutBizKey outBizKey;

    /**
     * 计数值
     * >0 正向计数
     * <0 负向计数
     */
    private Integer countValue;


    /**
     * 幂等单据号
     */
    private String idempotentNo;
}
