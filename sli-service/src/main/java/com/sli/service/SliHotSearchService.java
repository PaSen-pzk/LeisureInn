package com.sli.service;

import com.sli.common.model.dto.HotSearchDTO;
import com.sli.common.page.Page;
import com.sli.dao.entity.SliHotSearchDO;

import java.util.List;

public interface SliHotSearchService {

    /**
     * 保存热搜数据到数据库
     *
     * @param sbmyHotSearchDOList 数据库
     * @return 操作状态
     */
    Boolean saveCache2DB(List<SliHotSearchDO> sbmyHotSearchDOList);


    /**
     * 根据标题查询热搜
     *
     * @param title    标题
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 热搜
     */
    Page<HotSearchDTO> pageQueryHotSearchByTitle(String title, Integer pageNum, Integer pageSize);

}
