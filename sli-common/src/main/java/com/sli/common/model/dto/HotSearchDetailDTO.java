package com.sli.common.model.dto;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotSearchDetailDTO {

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 热搜数据
     */
    private List<HotSearchDTO> hotSearchDTOList;
}
