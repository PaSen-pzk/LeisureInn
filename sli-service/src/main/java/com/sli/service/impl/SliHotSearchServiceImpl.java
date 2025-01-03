package com.sli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.sli.common.model.dto.HotSearchDTO;
import com.sli.common.page.Page;
import com.sli.dao.AbstractBaseDO;
import com.sli.dao.entity.SliHotSearchDO;
import com.sli.dao.repository.SliHotSearchRepository;
import com.sli.service.SliHotSearchService;
import com.sli.service.convert.HotSearchConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        if (CollectionUtils.isNotEmpty(sliHotSearchDOList)) {
            List<String> tempIdList = sliHotSearchDOList.stream().map(SliHotSearchDO::getHotSearchId).collect(
                Collectors.toList());
            sliHotSearchDOS = sliHotSearchDOS.stream().filter(
                sliHotSearchDO -> !tempIdList.contains(sliHotSearchDO.getHotSearchId())).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(sliHotSearchDOS)) {
            return Boolean.TRUE;
        }
        log.info("本次新增[{}]条数据", sliHotSearchDOS.size());
        //批量添加
        return sliHotSearchRepository.saveBatch(sliHotSearchDOS);
    }

    @Override
    public Page<HotSearchDTO> pageQueryHotSearchByTitle(String title, Integer pageNum, Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //查询热搜
        List<SliHotSearchDO> sliHotSearchDOS = sliHotSearchRepository.list(
            new QueryWrapper<SliHotSearchDO>().lambda().like(SliHotSearchDO::getHotSearchTitle, "%" + title + "%")
                .orderByDesc(AbstractBaseDO::getGmtCreate));
        if (CollectionUtils.isEmpty(sliHotSearchDOS)) {
            return Page.emptyPage();
        }
        //对象转换
        return Page.resetPage(sliHotSearchDOS, sliHotSearchDOS.stream().map(HotSearchConvert::toDTOWhenQuery)
            .collect(Collectors.toList()));
    }
}
