package com.sli.service.convert;

import com.sli.common.model.dto.HotSearchDTO;
import com.sli.dao.entity.SliHotSearchDO;

public class HotSearchConvert {
    public static HotSearchDTO toDTOWhenQuery(SliHotSearchDO sliHotSearchDO) {
        return HotSearchDTO.builder()
            //热搜作者
            .hotSearchAuthor(sliHotSearchDO.getHotSearchAuthor())
            //热搜作者头像
            .hotSearchAuthorAvatar(sliHotSearchDO.getHotSearchAuthorAvatar())
            //热搜封面
            .hotSearchCover(sliHotSearchDO.getHotSearchCover())
            //热度
            .hotSearchHeat(sliHotSearchDO.getHotSearchHeat())
            //热搜id
            .hotSearchId(sliHotSearchDO.getHotSearchId())
            //热搜摘要
            .hotSearchExcerpt(sliHotSearchDO.getHotSearchExcerpt())
            //热搜排序
            .hotSearchOrder(sliHotSearchDO.getHotSearchOrder())
            //热搜来源
            .hotSearchResource(sliHotSearchDO.getHotSearchResource())
            //热搜标题
            .hotSearchTitle(sliHotSearchDO.getHotSearchTitle())
            //热搜链接
            .hotSearchUrl(sliHotSearchDO.getHotSearchUrl())
            //物理主键
            .id(sliHotSearchDO.getId()).build();
    }
}
