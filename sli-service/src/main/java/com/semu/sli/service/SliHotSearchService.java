package com.semu.sli.service;

import com.semu.sli.dao.entity.SliHotSearchDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SliHotSearchService {

    Boolean saveCache2DB(List<SliHotSearchDO> sliHotSearchDOList);
}
