package com.semu.sli.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.semu.sli.dao.entity.SliHotSearchDO;
import com.semu.sli.dao.mapper.SliHotSearchMapper;
import com.semu.sli.dao.repository.SliHotSearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SliHotSearchRepositoryImpl extends ServiceImpl<SliHotSearchMapper, SliHotSearchDO> implements SliHotSearchRepository {


}
