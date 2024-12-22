package com.sli.cache.hotSearch;

import com.google.common.collect.Maps;
import com.sli.common.model.dto.HotSearchDetailDTO;

import java.util.Map;

public class HotSearchCacheManager {

    /**
     * 热搜缓存
     */
    public static final Map<String, HotSearchDetailDTO> CACHE_MAP = Maps.newHashMap();
}
