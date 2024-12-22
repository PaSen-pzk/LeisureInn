package com.sli.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sli.dao.entity.SliSysConfigDO;
import com.sli.dao.mapper.SliSysConfigMapper;
import com.sli.dao.repository.SliSysConfigRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SliSysConfigRepositoryImpl extends ServiceImpl<SliSysConfigMapper, SliSysConfigDO> implements SliSysConfigRepository {
}
