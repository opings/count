package com.hupu.deep.light.helper;

import com.hupu.deep.comment.entity.LightRecordDO;
import com.hupu.deep.comment.enums.LightStatusEnum;
import com.hupu.deep.comment.model.LightRecordModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.foundation.util.AssertUtil;
import org.springframework.stereotype.Service;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 16:27
 */
@Service
public class LightHelper {


    public LightRecordModel buildLightRecordModel(LightRecordDO lightRecordDO) {
        if (null == lightRecordDO) {
            return null;
        }
        LightRecordModel lightRecordModel = new LightRecordModel();
        lightRecordModel.setUserId(lightRecordDO.getUserId());
        lightRecordModel.setSubjectId(lightRecordDO.getSubjectId());
        lightRecordModel.setLightStatusEnum(LightStatusEnum.getByCode(lightRecordDO.getLightStatus()));
        return lightRecordModel;
    }


    /**
     * 构造总点亮数 计数key
     *
     * @param subjectId
     * @return
     */
    public OutBizKey buildTotalLightCounterKey(String subjectId) {
        
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        return new OutBizKey("light_total_counter", subjectId);
    }


}
