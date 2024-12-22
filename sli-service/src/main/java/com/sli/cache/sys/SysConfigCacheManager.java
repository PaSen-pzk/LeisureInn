package com.sli.cache.sys;

import com.google.common.collect.Maps;
import com.sli.dao.entity.SliSysConfigDO;
import com.sli.dao.repository.SliSysConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 系统配置读取
 */
@Component
@Slf4j
public class SysConfigCacheManager {

    @Autowired
    private SliSysConfigRepository sliSysConfigRepository;

    /**
     * 加个锁，防止出现并发问题
     */
    private static final Lock LOCK = new ReentrantLock();

    /**
     * 底层缓存组件，可以使用ConcurrentMap也可以使用Redis，推荐使用Redis
     */
    private static ConcurrentMap<String, List<SliSysConfigDO>> CACHE = Maps.newConcurrentMap();
    /**
     * 底层缓存组件，可以使用ConcurrentMap也可以使用Redis，推荐使用Redis
     */
    private static ConcurrentMap<String, String> KEY_CACHE = Maps.newConcurrentMap();

    @Scheduled(fixedRate = 60 * 1000)
    public void loadingCache() {
        LOCK.lock();
        try {
            List<SliSysConfigDO> sliSysConfigDOS = sliSysConfigRepository.list();
            if (CollectionUtils.isEmpty(sliSysConfigDOS)) {
                return;
            }
            //按key缓存
            sliSysConfigDOS.forEach(sbmySysConfigDO -> {
                KEY_CACHE.put(sbmySysConfigDO.getGroupCode() + "_" + sbmySysConfigDO.getItemKey(), sbmySysConfigDO.getItemValue());
            });
            //按组缓存
            Map<String, List<SliSysConfigDO>> configMap = sliSysConfigDOS.stream().collect(Collectors.groupingBy(SliSysConfigDO::getGroupCode));
            configMap.forEach((key, value) -> {
                CACHE.put(key, value);
            });
        } catch (Exception e) {
            log.error("获取系统缓存数据异常", e);
        } finally {
            LOCK.unlock();
        }
    }

    public static List<SliSysConfigDO> getConfigByGroupCode(String groupCode) {
        return CACHE.get(groupCode);
    }

    public static String getConfigByGroupCodeAndKey(String groupCode, String key) {
        return KEY_CACHE.get(groupCode + "_" + key);
    }
}
