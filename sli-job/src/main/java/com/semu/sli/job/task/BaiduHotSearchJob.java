package com.semu.sli.job.task;

import com.semu.sli.common.utils.IdUtils;
import com.semu.sli.dao.entity.SliHotSearchDO;
import com.semu.sli.service.SliHotSearchService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.semu.sli.common.enums.HotSearchSource.BAIDU;

/**
 * 百度热搜排行抓取任务
 */
@Slf4j
@Component
public class BaiduHotSearchJob {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    /**
     * 定时触发爬虫方法，1个小时执行一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void baiduHotSearchJob() {
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
                sliHotSearchDO.setHotSearchId(IdUtils.getHashId(BAIDU.getDesc() + sliHotSearchDO.getHotSearchTitle()));
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
            //数据持久化
            sliHotSearchService.saveCache2DB(sliHotSearchDOList);
        } catch (IOException e) {
            log.error("获取百度数据异常", e);
        }
    }
}
