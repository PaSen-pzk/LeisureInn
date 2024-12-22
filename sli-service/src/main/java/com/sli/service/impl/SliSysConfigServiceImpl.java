package com.sli.service.impl;

import com.sli.cache.sys.SysConfigCacheManager;
import com.sli.common.model.dto.SkylineWebcamsDTO;
import com.sli.dao.entity.SliSysConfigDO;
import com.sli.dao.repository.SliSysConfigRepository;
import com.sli.service.SliSysConfigService;
import com.sli.service.convert.SliSysConfigConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SliSysConfigServiceImpl implements SliSysConfigService {
    @Autowired
    private SliSysConfigRepository sliSysConfigRepository;

    @Override
    public List<SkylineWebcamsDTO> querySkylineWebcams() {
        List<SliSysConfigDO> skylineWebcams = SysConfigCacheManager.getConfigByGroupCode("SkylineWebcams");
        //随机排列顺序
        Collections.shuffle(skylineWebcams);
        return skylineWebcams.subList(0, 3).stream().map(SliSysConfigConvert::toSkylineWebcamsDTOWhenQuery).collect(
            Collectors.toList());
    }
}
