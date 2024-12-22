package com.sli.service;

import com.sli.common.model.dto.VisitorCountDTO;

import java.util.Date;

public interface SliVisitLogService {

    /**
     * 查询pv/uv
     *
     * @param startTime 开始时间
     * @param entTime   结束时间
     * @return pv/uv
     */
    VisitorCountDTO queryVisitorCount(Date startTime, Date entTime);
}
