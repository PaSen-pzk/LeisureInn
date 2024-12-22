package com.sli.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sli.dao.entity.SliHotSearchDO;
import com.sli.dao.mapper.SliHotSearchMapper;
import com.sli.dao.repository.SliHotSearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SliHotSearchRepositoryImpl extends ServiceImpl<SliHotSearchMapper, SliHotSearchDO>
    implements SliHotSearchRepository {

}
