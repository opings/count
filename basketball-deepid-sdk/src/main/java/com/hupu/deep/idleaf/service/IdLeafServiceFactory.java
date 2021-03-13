package com.hupu.deep.idleaf.service;

import com.hupu.deep.idleaf.service.support.MysqlIdLeafServiceImpl;
import com.hupu.foundation.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jiangfangyuan
 * @since 2020-03-15 17:33
 */
@Service
public class IdLeafServiceFactory {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ConcurrentHashMap<String, IdLeafService> bizLeafServiceMap = new ConcurrentHashMap<>();

    /**
     * 根据业务标签获取相应的id，这样可以动态的创建相应的服务，而不需要停服，
     *
     * @param bizTag
     * @return
     */
    public Long genId(String bizTag) {

        AssertUtil.notBlank(bizTag, () -> "bizTag empty");
        return getIdLeafService(bizTag).genId();
    }


    /**
     * 基因法生成Id
     *
     * @param bizTag
     * @param userId
     * @param dbSize
     * @return
     */
    public Long genId(String bizTag, Long userId, Integer dbSize) {

        AssertUtil.notBlank(bizTag, () -> "bizTag empty");
        AssertUtil.notNull(dbSize, () -> "dbSize null");
        IdLeafService idLeafService = getIdLeafService(bizTag);
        return new WithGeneIdLeafService(idLeafService, dbSize)
                .getId(userId);
    }

    private IdLeafService getIdLeafService(String bizTag) {
        if (bizLeafServiceMap.get(bizTag) == null) {
            synchronized (bizLeafServiceMap) {
                if (bizLeafServiceMap.get(bizTag) == null) {
                    IdLeafService idLeafService = new MysqlIdLeafServiceImpl(bizTag, jdbcTemplate, true);
                    bizLeafServiceMap.putIfAbsent(bizTag, idLeafService);
                }
            }
        }
        return bizLeafServiceMap.get(bizTag);
    }

}
