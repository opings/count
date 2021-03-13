package com.hupu.deep.black.helper;

import com.hupu.deep.comment.entity.BlackRecordDO;
import com.hupu.deep.comment.enums.BlackStatusEnum;
import com.hupu.deep.comment.model.BlackRecordModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.foundation.util.AssertUtil;
import org.springframework.stereotype.Service;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 16:27
 */
@Service
public class BlackHelper {


    public BlackRecordModel buildBlackRecordModel(BlackRecordDO blackRecordDO) {
        if (null == blackRecordDO) {
            return null;
        }
        BlackRecordModel blackRecordModel = new BlackRecordModel();
        blackRecordModel.setUserId(blackRecordDO.getUserId());
        blackRecordModel.setSubjectId(blackRecordDO.getSubjectId());
        blackRecordModel.setBlackStatusEnum(BlackStatusEnum.getByCode(blackRecordDO.getBlackStatus()));
        return blackRecordModel;
    }


    /**
     * 构造总点灭数 计数key
     *
     * @param subjectId
     * @return
     */
    public OutBizKey buildTotalBlackCounterKey(String subjectId) {
        
        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        return new OutBizKey("black_total_counter", subjectId);
    }


}
