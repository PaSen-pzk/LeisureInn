package com.semu.web.controller;

import com.sli.common.model.dto.HotSearchDTO;
import com.sli.common.model.dto.HotSearchDetailDTO;
import com.sli.common.page.Page;
import com.sli.common.result.ResultModel;
import com.sli.service.SliHotSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.sli.cache.hotSearch.HotSearchCacheManager.CACHE_MAP;

@RestController
@RequestMapping("/api/hotSearch")
public class HotSearchController {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    @GetMapping("/queryByType")
    public ResultModel<HotSearchDetailDTO> queryByType(@RequestParam(required = true) String type) {
        return ResultModel.success(CACHE_MAP.get(type.toUpperCase()));
    }

    @GetMapping("/pageQueryHotSearchByTitle")
    public ResultModel<Page<HotSearchDTO>> pageQueryHotSearchByTitle(@RequestParam(required = true) String title,
                                                                     @RequestParam(required = true) Integer pageNum, @RequestParam(required = true) Integer pageSize) {
        return ResultModel.success(sliHotSearchService.pageQueryHotSearchByTitle(title, pageNum, pageSize));
    }
}
