package com.sli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sli.dao.entity.SliHotSearchDO;
import com.sli.dao.repository.SliHotSearchRepository;
import com.sli.service.SliHotSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SliHotSearchServiceImpl implements SliHotSearchService {

    @Autowired
    private SliHotSearchRepository sliHotSearchRepository;

    @Override
    public Boolean saveCache2DB(List<SliHotSearchDO> sliHotSearchDOS) {
        if (CollectionUtils.isEmpty(sliHotSearchDOS)) {
            return Boolean.TRUE;
        }
        //查询当前数据是否已经存在
        List<String> searchIdList = sliHotSearchDOS.stream().map(SliHotSearchDO::getHotSearchId).collect(
                Collectors.toList());
        List<SliHotSearchDO> sliHotSearchDOList = sliHotSearchRepository.list(
                new QueryWrapper<SliHotSearchDO>().lambda().in(SliHotSearchDO::getHotSearchId, searchIdList));
        //过滤已经存在的数据
        if (!CollectionUtils.isEmpty(sliHotSearchDOList)) {
            List<String> tempIdList = sliHotSearchDOList.stream().map(SliHotSearchDO::getHotSearchId).collect(
                    Collectors.toList());
            sliHotSearchDOS = sliHotSearchDOS.stream().filter(
                    sbmyHotSearchDO -> !tempIdList.contains(sbmyHotSearchDO.getHotSearchId())).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(sliHotSearchDOS)) {
            return Boolean.TRUE;
        }
        log.info("本次新增[{}]条数据", sliHotSearchDOS.size());
        //批量添加
        return sliHotSearchRepository.saveBatch(sliHotSearchDOS);
    }
}
