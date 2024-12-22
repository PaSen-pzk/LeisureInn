package com.sli.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sli.dao.AbstractBaseDO;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@TableName("t_sli_hot_search")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SliHotSearchDO  extends AbstractBaseDO<SliHotSearchDO> {
    /**
     * 物理主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 热搜标题
     */
    @Column(name = "hot_search_title")
    private String hotSearchTitle;

    /**
     * 热搜作者
     */
    @Column(name = "hot_search_author")
    private String hotSearchAuthor;

    /**
     * 热搜来源
     */
    @Column(name = "hot_search_resource")
    private String hotSearchResource;

    /**
     * 热搜排名
     */
    @Column(name = "hot_search_order")
    private Integer hotSearchOrder;

    /**
     * 热搜ID
     */
    @Column(name = "hot_search_id")
    private String hotSearchId;

    /**
     * 热搜热度
     */
    @Column(name = "hot_search_heat")
    private String hotSearchHeat;

    /**
     * 热搜链接
     */
    @Column(name = "hot_search_url")
    private String hotSearchUrl;

    /**
     * 热搜封面
     */
    @Column(name = "hot_search_cover")
    private String hotSearchCover;

    /**
     * 热搜作者头像
     */
    @Column(name = "hot_search_author_avatar")
    private String hotSearchAuthorAvatar;

    /**
     * 热搜摘录
     */
    @Column(name = "hot_search_excerpt")
    private String hotSearchExcerpt;
}
