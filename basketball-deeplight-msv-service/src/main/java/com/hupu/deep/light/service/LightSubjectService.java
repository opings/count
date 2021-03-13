package com.hupu.deep.light.service;

import com.google.common.collect.Lists;
import com.hupu.deep.comment.dal.LightRecordMapper;
import com.hupu.deep.comment.dal.LightSubjectMapper;
import com.hupu.deep.comment.entity.LightSubjectDO;
import com.hupu.deep.comment.enums.BizTypeEnum;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.CacheConfig;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.count.service.CountService;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.deep.light.helper.LightHelper;
import com.hupu.foundation.cache.CacheClient;
import com.hupu.foundation.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangfangyuan
 * @since 2020-03-12 13:28
 */
@Service
@Slf4j
public class LightSubjectService {

    @Autowired
    private LightSubjectMapper lightSubjectMapper;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private CountService countService;

    @Autowired
    private LightHelper lightHelper;

    @Autowired
    private IdService idService;

    @Autowired
    private LightRecordMapper lightRecordMapper;


    public Integer getLightCount(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        //点亮数
        String lightSubjectId = getSubjectId(outBizKey);
        if (StringUtils.isBlank(lightSubjectId)) {
            return Globals.NUM_0;
        }
        return getLightCount(lightSubjectId);
    }


    public Integer getLightCount(String subjectId) {

        AssertUtil.notBlank(subjectId, () -> "subjectId empty");

        Integer counter = countService.queryCountValue(lightHelper.buildTotalLightCounterKey(subjectId));
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

        return cacheClient.get(CacheConfig.LIGHT, outBizKey.getKey(), () -> {
            LightSubjectDO lightSubjectDO = lightSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo());
            return Optional.ofNullable(lightSubjectDO).map(LightSubjectDO::getSubjectId).orElse(null);
        }, Globals.NUM_60);
    }

    /**
     * 创建点亮主体
     *
     * @param outBizKey
     * @return
     */
    public String createSubject(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        try {
            String subjectId = idService.genId(BizTypeEnum.LIGHT.getCode()).toString();
            LightSubjectDO lightSubjectDO = new LightSubjectDO();
            lightSubjectDO.setSubjectId(subjectId);
            lightSubjectDO.setOutBizType(outBizKey.getOutBizType());
            lightSubjectDO.setOutBizNo(outBizKey.getOutBizNo());
            lightSubjectMapper.insertSelective(lightSubjectDO);
            return subjectId;
        } catch (DuplicateKeyException ex) {
            log.info("重复创建点亮主体 " + outBizKey);
            return lightSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo())
                    .getSubjectId();
        }
    }

    public List<Long> getLightUserIdList(OutBizKey outBizKey) {
        AssertUtil.notNull(outBizKey, () -> "outBizKey null");
        String lightSubjectId = getSubjectId(outBizKey);
        if (StringUtils.isBlank(lightSubjectId)) {
            return Lists.newArrayList();
        }
        return lightRecordMapper.selectLightRecord(lightSubjectId).stream().map(lr->lr.getUserId()).collect(Collectors.toList());
    }

}
