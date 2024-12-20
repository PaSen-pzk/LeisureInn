package com.sli.service;

import com.sli.dao.entity.SliHotSearchDO;

import java.util.List;

public interface SliHotSearchService {

    Boolean saveCache2DB(List<SliHotSearchDO> sliHotSearchDOList);
}
