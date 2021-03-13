package com.hupu.deep;


import com.hupu.deep.comment.types.OutBizKey;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 21:00
 */
public interface OutBizAble {

    /**
     * 外部业务类型
     *
     * @return
     */
    String getOutBizType();

    /**
     * 外部业务单号
     *
     * @return
     */
    String getOutBizNo();

    /**
     * 获取OutBizKey
     * @return
     */
    default OutBizKey getOutBizKey() {
        return new OutBizKey(getOutBizType(), getOutBizNo());
    }
}
