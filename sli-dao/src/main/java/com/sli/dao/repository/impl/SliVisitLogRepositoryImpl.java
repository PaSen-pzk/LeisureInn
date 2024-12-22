package com.sli.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sli.dao.entity.SliVisitLogDO;
import com.sli.dao.mapper.SliVisitLogMapper;
import com.sli.dao.repository.SliVisitLogRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SliVisitLogRepositoryImpl extends ServiceImpl<SliVisitLogMapper, SliVisitLogDO> implements SliVisitLogRepository {
    @Override
    public int queryUvByStartTimeAndEndTime(Date startTime, Date endTime) {
        return this.baseMapper.queryUvByStartTimeAndEndTime(startTime, endTime);
    }

    @Override
    public int queryUv() {
        return this.baseMapper.queryUv();
    }
}
