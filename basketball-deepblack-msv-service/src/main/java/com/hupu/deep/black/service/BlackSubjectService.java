package com.hupu.deep.black.service;

import com.hupu.deep.comment.dal.BlackSubjectMapper;
import com.hupu.deep.comment.entity.BlackSubjectDO;
import com.hupu.deep.comment.enums.BizTypeEnum;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.CacheConfig;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.count.service.CountService;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.deep.black.helper.BlackHelper;
import com.hupu.foundation.cache.CacheClient;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author jiangfangyuan
 * @since 2020-03-12 13:28
 */
@Service
@Slf4j
public class BlackSubjectService {

    @Autowired
    private BlackSubjectMapper blackSubjectMapper;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private CountService countService;

    @Autowired
    private BlackHelper blackHelper;

    @Autowired
    private IdService idService;


    public Integer getBlackCount(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        //点灭数
        String blackSubjectId = getSubjectId(outBizKey);
        if (StringUtils.isBlank(blackSubjectId)) {
            return Globals.NUM_0;
        }
        return getBlackCount(blackSubjectId);
    }


    public Integer getBlackCount(String subjectId) {

        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        Integer counter = countService.queryCountValue(blackHelper.buildTotalBlackCounterKey(subjectId));
        return Optional.ofNullable(counter).orElse(Globals.NUM_0);
    }


    public String getOrCreateSubjectId(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        String subjectId = getSubjectId(outBizKey);
        if (StringUtils.isBlank(subjectId)) {
            return createSubject(outBizKey);
        }
        return subjectId;
    }


    public String getSubjectId(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        return cacheClient.get(CacheConfig.BLACK, outBizKey.getKey(), () -> {
            BlackSubjectDO blackSubjectDO = blackSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo());
            return Optional.ofNullable(blackSubjectDO).map(BlackSubjectDO::getSubjectId).orElse(null);
        }, Globals.NUM_60);
    }

    /**
     * 创建点灭主体
     *
     * @param outBizKey
     * @return
     */
    public String createSubject(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        try {
            String subjectId = idService.genId(BizTypeEnum.BLACK.getCode()).toString();
            BlackSubjectDO blackSubjectDO = new BlackSubjectDO();
            blackSubjectDO.setSubjectId(subjectId);
            blackSubjectDO.setOutBizType(outBizKey.getOutBizType());
            blackSubjectDO.setOutBizNo(outBizKey.getOutBizNo());
            blackSubjectMapper.insertSelective(blackSubjectDO);
            return subjectId;
        } catch (DuplicateKeyException ex) {
            log.info("重复创建点灭主体 " + outBizKey);
            return blackSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo())
                    .getSubjectId();
        }
    }
}
