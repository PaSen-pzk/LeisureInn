package com.sli.job.baidu;

import com.sli.common.model.dto.HotSearchDetailDTO;
import com.sli.dao.entity.SliHotSearchDO;
import com.sli.service.SliHotSearchService;
import com.sli.service.convert.HotSearchConvert;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.sli.cache.hotSearch.HotSearchCacheManager.CACHE_MAP;
import static com.sli.common.enums.HotSearchSource.BAIDU;

/**
 * @author summo
 * @version BaiduHotSearchJob.java, 1.0.0
 * @description 百度热搜Java爬虫代码
 * @date 2024年08月19
 */
@Component
@Slf4j
public class BaiduHotSearchJob {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    @XxlJob("baiduHotSearchJob")
    public ReturnT<String> hotSearch(String param) throws IOException {
         log.info("百度热搜爬虫任务开始");
        try {
            //获取百度热搜
            String url = "https://top.baidu.com/board?tab=realtime&sa=fyb_realtime_31065";
            List<SliHotSearchDO> sliHotSearchDOList = new ArrayList<>();
            Document doc = Jsoup.connect(url).get();
            //标题
            Elements titles = doc.select(".c-single-text-ellipsis");
            //图片
            Elements imgs = doc.select(".category-wrap_iQLoo .index_1Ew5p").next("img");
            //内容
            Elements contents = doc.select(".hot-desc_1m_jR.large_nSuFU");
            //推荐图
            Elements urls = doc.select(".category-wrap_iQLoo a.img-wrapper_29V76");
            //热搜指数
            Elements levels = doc.select(".hot-index_1Bl1a");
            for (int i = 0; i < levels.size(); i++) {
                SliHotSearchDO sliHotSearchDO = SliHotSearchDO.builder().hotSearchResource(BAIDU.getCode()).build();
                //设置文章标题
                sliHotSearchDO.setHotSearchTitle(titles.get(i).text().trim());
                //设置百度三方ID
                sliHotSearchDO.setHotSearchId(getHashId(BAIDU.getDesc() + sliHotSearchDO.getHotSearchTitle()));
                //设置文章封面
                sliHotSearchDO.setHotSearchCover(imgs.get(i).attr("src"));
                //设置文章摘要
                sliHotSearchDO.setHotSearchExcerpt(contents.get(i).text().replaceAll("查看更多>", ""));
                //设置文章连接
                sliHotSearchDO.setHotSearchUrl(urls.get(i).attr("href"));
                //设置热搜热度
                sliHotSearchDO.setHotSearchHeat(levels.get(i).text().trim());
                //按顺序排名
                sliHotSearchDO.setHotSearchOrder(i + 1);
                sliHotSearchDOList.add(sliHotSearchDO);
            }
            if (CollectionUtils.isEmpty(sliHotSearchDOList)) {
                return ReturnT.SUCCESS;
            }
            //数据加到缓存中
            CACHE_MAP.put(BAIDU.getCode(), HotSearchDetailDTO.builder()
                //热搜数据
                .hotSearchDTOList(
                    sliHotSearchDOList.stream().map(HotSearchConvert::toDTOWhenQuery).collect(Collectors.toList()))
                //更新时间
                .updateTime(Calendar.getInstance().getTime()).build());
            //数据持久化
            sliHotSearchService.saveCache2DB(sliHotSearchDOList);
            log.info("百度热搜爬虫任务结束");
        } catch (IOException e) {
            log.error("获取百度数据异常", e);
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 根据文章标题获取一个唯一ID
     *
     * @param title 文章标题
     * @return 唯一ID
     */
    public static String getHashId(String title) {
        long seed = title.hashCode();
        Random rnd = new Random(seed);
        return new UUID(rnd.nextLong(), rnd.nextLong()).toString();
    }

    @PostConstruct
    public void init() {
        // 启动运行爬虫一次
        try {
            hotSearch(null);
        } catch (IOException e) {
            log.error("启动爬虫脚本失败",e);
        }
    }

}

