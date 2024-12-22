package com.sli.service;

import com.sli.common.model.dto.SkylineWebcamsDTO;

import java.util.List;

public interface SliSysConfigService {
    List<SkylineWebcamsDTO> querySkylineWebcams();
}
