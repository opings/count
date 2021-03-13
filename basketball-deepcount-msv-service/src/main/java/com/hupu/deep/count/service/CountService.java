package com.hupu.deep.count.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hupu.deep.comment.dal.CountRecordMapper;
import com.hupu.deep.comment.dal.CountSubjectMapper;
import com.hupu.deep.comment.dal.SplitCountSubjectMapper;
import com.hupu.deep.comment.entity.CountRecordDO;
import com.hupu.deep.comment.entity.CountSubjectDO;
import com.hupu.deep.comment.entity.SplitCountSubjectDO;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.idleaf.service.IdService;
import com.hupu.foundation.cache.CountClient;
import com.hupu.foundation.util.AssertUtil;
import com.hupu.foundation.util.SplitterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author jiangfangyuan
 * @since 2020-03-15 22:34
 */
@Service
@Slf4j
public class CountService {

    private String nameSpace = "bpl_counter_msv_service";

    private Integer ttl = 1;


    @Autowired
    private CountSubjectMapper countSubjectMapper;

    @Autowired
    private SplitCountSubjectMapper splitCountSubjectMapper;

    @Autowired
    private CountRecordMapper countRecordMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CountClient countClient;

    @Autowired
    private IdService idService;

    private String counter = "COUNTER";

    /**
     * 发布 或 更新 计数流水
     * >0 正向计数
     * <0 负向计数
     *
     * @param outBizKey
     * @param countValue
     */
    public int publishOrUpdateCountFlow(OutBizKey outBizKey, Integer countValue, String idempotentNo) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");
        AssertUtil.notNull(countValue, () -> "countValue null");
        AssertUtil.notBlank(idempotentNo, () -> "idempotentNo empty");

        log.info("开始计数 outBizKey {} countValue {} idempotentNo {}", outBizKey, countValue, idempotentNo);

        CountSubjectDO countSubjectDO = getOrCreateCountSubjectDO(outBizKey);

        Integer splitSubjectCount = countSubjectDO.getSplitSubjectCount();
        AssertUtil.notNull(splitSubjectCount, () -> "splitSubjectCount null");
        List<String> splitSubjectIdList = SplitterUtil.toList(countSubjectDO.getSplitSubjectId());

        //计数主体分裂
        if (splitSubjectCount - splitSubjectIdList.size() > 0) {
            createSplitSubject(splitSubjectCount - splitSubjectIdList.size(), countSubjectDO);
            countSubjectDO = getCountSubjectDO(outBizKey);
        }

        String singleSplitSubjectId = routeSplitSubjectId(countSubjectDO, idempotentNo);
        if (null == countRecordMapper.selectByIdempotentNo(singleSplitSubjectId, idempotentNo)) {
            Integer counterResult = transactionTemplate.execute(transactionStatus -> {
                try {
                    CountRecordDO countRecordDO = new CountRecordDO();
                    countRecordDO.setSubjectId(singleSplitSubjectId);
                    countRecordDO.setCountRecordId(idService.genId(counter).toString());
                    countRecordDO.setCountValue(countValue);
                    countRecordDO.setIdempotentNo(idempotentNo);
                    countRecordMapper.insertSelective(countRecordDO);
                    splitCountSubjectMapper.increaseCountValue(singleSplitSubjectId, countValue);
                    return countValue;
                } catch (DuplicateKeyException ex) {
                    log.warn("重复创建计数记录 {} {}", singleSplitSubjectId, idempotentNo);
                }
                return null;
            });
            if (Objects.equals(counterResult, countValue)) {
                countClient.increase(nameSpace, outBizKey.getKey(),
                        queryCountValueSupplier(outBizKey), ttl, TimeUnit.MINUTES,
                        counterResult);
                return countValue;
            }
        }
        Integer counterResult = transactionTemplate.execute(transactionStatus -> {
            //1 锁定原始记录
            CountRecordDO countRecordDO = countRecordMapper.selectByIdempotentNoForUpdate(singleSplitSubjectId, idempotentNo);

            log.info("命中 幂等 更新原始计数流水 singleSplitSubjectId {} idempotentNo {} dbValue {} toUpdateValue {}",
                    singleSplitSubjectId, idempotentNo, countRecordDO.getCountValue(), countValue);

            //2 用当前值更新
            countRecordMapper.updateCountValue(singleSplitSubjectId, idempotentNo, countValue);


            Integer incrementalValue = countValue - countRecordDO.getCountValue();

            //3 增量汇总
            splitCountSubjectMapper.increaseCountValue(singleSplitSubjectId, incrementalValue);

            //3 返回增量
            return incrementalValue;
        });
        countClient.increase(nameSpace, outBizKey.getKey(),
                queryCountValueSupplier(outBizKey), ttl, TimeUnit.MINUTES,
                counterResult);
        return counterResult;
    }

    /**
     * 计数列表
     *
     * @param outBizKey
     * @return
     */
    public List<CountRecordDO> countRecordList(OutBizKey outBizKey) {

        AssertUtil.notNull(outBizKey, () -> "outBizKey null");

        List<CountRecordDO> resultList = Lists.newArrayList();

        CountSubjectDO countSubjectDO = getOrCreateCountSubjectDO(outBizKey);
        for (String splitSubjectId : SplitterUtil.toList(countSubjectDO.getSplitSubjectId())) {
            List<CountRecordDO> countRecordDOList = countRecordMapper.selectListBySubjectId(splitSubjectId);
            resultList.addAll(countRecordDOList);
        }
        return resultList;
    }

    /**
     * 查询总计数值
     *
     * @param outBizKey
     * @return
     */
    public Integer queryCountValue(OutBizKey outBizKey) {

        Integer countValue = countClient.getCount(nameSpace, outBizKey.getKey());
        if (countValue != null) {
            return countValue;
        }
        countClient.putIfAbsentCount(nameSpace, outBizKey.getKey(), queryCountValueSupplier(outBizKey).get(), ttl, TimeUnit.MINUTES);
        return countClient.getCount(nameSpace, outBizKey.getKey());
    }


    /**
     * 根据idempotentNo 路由分裂计数主体id.
     *
     * @param countSubjectDO
     * @param idempotentNo
     * @return
     */
    private String routeSplitSubjectId(CountSubjectDO countSubjectDO, String idempotentNo) {

        List<String> splitSubjectIdList = SplitterUtil.toList(countSubjectDO.getSplitSubjectId());
        String hashKey = countSubjectDO.getSubjectId() + ":" + idempotentNo;
        return splitSubjectIdList.get((Math.abs(hashKey.hashCode()) % splitSubjectIdList.size()));
    }


    private Supplier<Integer> queryCountValueSupplier(OutBizKey outBizKey) {
        return () -> {
            CountSubjectDO countSubjectDO = getCountSubjectDO(outBizKey);
            if (countSubjectDO == null) {
                return 0;
            }
            List<String> splitSubjectIdList = SplitterUtil.toList(countSubjectDO.getSplitSubjectId());
            return splitSubjectIdList.stream().map(subjectId -> splitCountSubjectMapper.selectBySubjectId(subjectId))
                    .filter(Objects::nonNull)
                    .map(SplitCountSubjectDO::getCountValue)
                    .filter(Objects::nonNull)
                    .mapToInt(item -> item).sum();
        };
    }


    private CountSubjectDO getOrCreateCountSubjectDO(OutBizKey outBizKey) {

        CountSubjectDO countSubjectDO = getCountSubjectDO(outBizKey);
        if (null == countSubjectDO) {
            try {
                countSubjectDO = new CountSubjectDO();
                countSubjectDO.setSubjectId(idService.genId(counter).toString());
                countSubjectDO.setOutBizType(outBizKey.getOutBizType());
                countSubjectDO.setOutBizNo(outBizKey.getOutBizNo());
                countSubjectDO.setSplitSubjectCount(Globals.NUM_5);
                countSubjectMapper.insertSelective(countSubjectDO);
            } catch (DuplicateKeyException ex) {
                return countSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo());
            }
        }
        return countSubjectDO;
    }

    private CountSubjectDO getCountSubjectDO(OutBizKey outBizKey) {
        return countSubjectMapper.selectByOutBizKey(outBizKey.getOutBizType(), outBizKey.getOutBizNo());
    }

    private void createSplitSubject(int num, CountSubjectDO parentSubjectDO) {
        transactionTemplate.execute(transactionStatus -> {

            //1 锁
            CountSubjectDO countSubjectDO = countSubjectMapper.selectBySubjectIdForUpdate(parentSubjectDO.getSubjectId());

            //2 判
            if (countSubjectDO.getSplitSubjectCount() <= SplitterUtil.toList(countSubjectDO.getSplitSubjectId()).size()) {
                return true;
            }

            //3 更新
            List<String> splitSubjectIdList = SplitterUtil.toList(parentSubjectDO.getSplitSubjectId());
            for (int i = 0; i < num; i++) {
                SplitCountSubjectDO splitCountSubjectDO = new SplitCountSubjectDO();
                splitCountSubjectDO.setSubjectId(idService.genId(counter).toString());
                splitCountSubjectDO.setParentSubjectId(parentSubjectDO.getSubjectId());
                splitCountSubjectMapper.insertSelective(splitCountSubjectDO);
                splitSubjectIdList.add(splitCountSubjectDO.getSubjectId());
            }
            countSubjectMapper.updateSplitSubjectId(parentSubjectDO.getSubjectId(),
                    Joiner.on(",").join(splitSubjectIdList));
            return true;
        });
    }

}
