package com.sli.service.convert;

import com.sli.common.model.dto.SkylineWebcamsDTO;
import com.sli.dao.entity.SliSysConfigDO;

public class SliSysConfigConvert {
    public static SkylineWebcamsDTO toSkylineWebcamsDTOWhenQuery(SliSysConfigDO sliSysConfigDO) {
        return SkylineWebcamsDTO.builder().placeName(sliSysConfigDO.getItemKey()).playUrl(
            sliSysConfigDO.getItemValue()).build();
    }
}
