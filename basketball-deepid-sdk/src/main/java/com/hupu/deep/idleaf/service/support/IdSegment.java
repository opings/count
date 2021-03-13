package com.hupu.deep.idleaf.service.support;

import lombok.Data;

import java.util.Date;

/**
 * @author jiangfangyuan
 * @since 2020-03-15 17:36
 */
@Data
public class IdSegment {

    private Long minId;
    private Long maxId;

    private Long step;

    private Long middleId;

    private Date lastUpdateTime;
    private Date currentUpdateTime;

    public IdSegment() {

    }

    public Long getMiddleId() {

        if (this.middleId == null) {
            this.middleId = this.maxId - (long) Math.round(step / 2);
        }
        return middleId;
    }

    public Long getMinId() {
        if (this.minId == null) {
            if (this.maxId != null && this.step != null) {
                this.minId = this.maxId - this.step;
            } else {
                throw new RuntimeException("maxid or step is null");
            }
        }

        return minId;
    }


    @Override
    public String toString() {
        return "IdSegment [minId=" + minId + ", maxId=" + maxId + ", step=" + step + ", middleId=" + middleId
                + ", lastUpdateTime=" + lastUpdateTime + ", currentUpdateTime=" + currentUpdateTime + "]";
    }

}