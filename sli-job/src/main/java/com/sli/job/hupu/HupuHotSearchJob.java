package com.sli.job.hupu;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.sli.cache.hotSearch.HotSearchCacheManager.CACHE_MAP;
import static com.sli.common.enums.HotSearchEnum.HUPU;

/**
 * @author summo
 * @version HupuHotSearchJob.java, 1.0.0
 * @description 虎扑热搜Java爬虫代码
 * @date 2024年08月09
 */
@Component
@Slf4j
public class HupuHotSearchJob {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    @XxlJob("hupuHotSearchJob")
    public ReturnT<String> hotSearch(String param) throws IOException {
        log.info("虎扑热搜爬虫任务开始");
        try {
            String url = "https://bbs.hupu.com/love-hot";
            List<SliHotSearchDO> sliHotSearchDOList = new ArrayList<>();
            Document doc = Jsoup.connect(url).get();
            //元素列表
            Elements elements = doc.select(".p-title");
            for (int i = 0; i < elements.size(); i++) {
                SliHotSearchDO sliHotSearchDO = SliHotSearchDO.builder().hotSearchResource(HUPU.getCode()).build();
                //设置文章标题
                sliHotSearchDO.setHotSearchTitle(elements.get(i).text().trim());
                //设置虎扑三方ID
                sliHotSearchDO.setHotSearchId(getHashId(HUPU.getCode() + sliHotSearchDO.getHotSearchTitle()));
                //设置文章连接
                sliHotSearchDO.setHotSearchUrl("https://bbs.hupu.com/" + doc.select(".p-title").get(i).attr("href"));
                //设置热搜热度
                sliHotSearchDO.setHotSearchHeat(doc.select(".post-datum").get(i).text().split("/")[1].trim());
                //设置热搜作者
                sliHotSearchDO.setHotSearchAuthor(doc.select(".post-auth").get(i).text());
                //按顺序排名
                sliHotSearchDOList.add(sliHotSearchDO);
            }
            AtomicInteger count = new AtomicInteger(1);
            sliHotSearchDOList = sliHotSearchDOList.stream().sorted(Comparator.comparingInt((SliHotSearchDO hotSearch) -> Integer.parseInt(hotSearch.getHotSearchHeat())).reversed()).map(sliHotSearchDO -> {
                sliHotSearchDO.setHotSearchOrder(count.getAndIncrement());
                return sliHotSearchDO;
            }).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(sliHotSearchDOList)) {
                return ReturnT.SUCCESS;
            }
            //数据加到缓存中
            CACHE_MAP.put(HUPU.getCode(), HotSearchDetailDTO.builder()
                    //热搜数据
                    .hotSearchDTOList(sliHotSearchDOList.stream().map(HotSearchConvert::toDTOWhenQuery).collect(Collectors.toList()))
                    //更新时间
                    .updateTime(Calendar.getInstance().getTime()).build());
            //数据持久化
            sliHotSearchService.saveCache2DB(sliHotSearchDOList);
            log.info("虎扑热搜爬虫任务结束");
        } catch (IOException e) {
            log.error("获取虎扑数据异常", e);
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 根据文章标题获取一个唯一ID
     *
     * @param title 文章标题
     * @return 唯一ID
     */
    private String getHashId(String title) {
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
