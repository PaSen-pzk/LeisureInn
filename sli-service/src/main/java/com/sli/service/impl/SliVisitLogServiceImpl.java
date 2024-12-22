package com.sli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sli.common.model.dto.VisitorCountDTO;
import com.sli.dao.AbstractBaseDO;
import com.sli.dao.entity.SliVisitLogDO;
import com.sli.dao.repository.SliVisitLogRepository;
import com.sli.service.SliVisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SliVisitLogServiceImpl implements SliVisitLogService {
    @Autowired
    private SliVisitLogRepository sliVisitLogRepository;

    @Override
    public VisitorCountDTO queryVisitorCount(Date startTime, Date endTime) {
        int todayPv = sliVisitLogRepository.count(
            new QueryWrapper<SliVisitLogDO>().lambda().between(AbstractBaseDO::getGmtCreate, startTime, endTime));
        int todayUv = sliVisitLogRepository.queryUvByStartTimeAndEndTime(startTime, endTime);
        int allPv = sliVisitLogRepository.count();
        int allUv = sliVisitLogRepository.queryUv();
        return VisitorCountDTO.builder()
            .todayPv(todayPv)
            .todayUv(todayUv)
            .allPv(allPv)
            .allUv(allUv)
            .build();
    }
}
