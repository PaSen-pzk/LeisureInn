package com.sli.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sli.dao.entity.SliVisitLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface SliVisitLogMapper extends BaseMapper<SliVisitLogDO> {

    @Select("SELECT COUNT(DISTINCT ip) FROM t_sbmy_visit_log WHERE gmt_create BETWEEN #{startTime} AND #{endTime}")
    int queryUvByStartTimeAndEndTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("SELECT COUNT(DISTINCT ip) FROM t_sbmy_visit_log")
    int queryUv();
}
