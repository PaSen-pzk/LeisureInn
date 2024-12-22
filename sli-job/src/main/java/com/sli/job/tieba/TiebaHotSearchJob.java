package com.sli.job.tieba;

import com.google.common.collect.Lists;
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
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.sli.cache.hotSearch.HotSearchCacheManager.CACHE_MAP;
import static com.sli.common.enums.HotSearchEnum.TIEBA;

/**
 * @author summo
 * @version TiebaHotSearchJob.java, 1.0.0
 * @description 贴吧热搜Java爬虫代码
 * @date 2024年08月09
 */
@Component
@Slf4j
public class TiebaHotSearchJob {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    @XxlJob("tiebaHotSearchJob")
    public ReturnT<String> hotSearch(String param) throws IOException {
        log.info("贴吧热搜爬虫任务开始");
        try {
            String url = "https://tieba.baidu.com/hottopic/browse/topicList?res_type=1";
            List<SliHotSearchDO> sliHotSearchDOList = Lists.newArrayList();
            Document doc = Jsoup.connect(url).get();
            //标题
            Elements titles = doc.select(".topic-top-item-desc");
            //热搜链接
            Elements urls = doc.select(".topic-text");
            //热搜指数
            Elements levels = doc.select(".topic-num");
            for (int i = 0; i < levels.size(); i++) {
                SliHotSearchDO sliHotSearchDO = SliHotSearchDO.builder().hotSearchResource(TIEBA.getCode()).build();
                //设置文章标题
                sliHotSearchDO.setHotSearchTitle(titles.get(i).text().trim());
                //设置文章连接
                sliHotSearchDO.setHotSearchUrl(urls.get(i).attr("href"));
                //设置知乎三方ID
                sliHotSearchDO.setHotSearchId(getValueFromUrl(sliHotSearchDO.getHotSearchUrl(), "topic_id"));
                //设置热搜热度
                sliHotSearchDO.setHotSearchHeat(levels.get(i).text().trim().replace("W实时讨论", "") + "万");
                //按顺序排名
                sliHotSearchDO.setHotSearchOrder(i + 1);
                sliHotSearchDOList.add(sliHotSearchDO);
            }
            if (CollectionUtils.isEmpty(sliHotSearchDOList)) {
                return ReturnT.SUCCESS;
            }
            //数据加到缓存中
            CACHE_MAP.put(TIEBA.getCode(), HotSearchDetailDTO.builder()
                //热搜数据
                .hotSearchDTOList(
                    sliHotSearchDOList.stream().map(HotSearchConvert::toDTOWhenQuery).collect(Collectors.toList()))
                //更新时间
                .updateTime(Calendar.getInstance().getTime()).build());
            //数据持久化
            sliHotSearchService.saveCache2DB(sliHotSearchDOList);
            log.info("贴吧热搜爬虫任务结束");
        } catch (Exception e) {
            log.error("获取贴吧数据异常", e);
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 从链接中获取参数
     *
     * @param url   链接
     * @param param 想要提取出值的参数
     * @return
     * @throws Exception
     */
    public static String getValueFromUrl(String url, String param) {
        if (Objects.isNull(url) || Objects.isNull(param)) {
            throw new RuntimeException("从链接中获取参数异常，url 或 param 为空");
        }
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            if (query == null) {
                return null;
            }
            Map<String, String> queryPairs = new HashMap<>();
            for (String pair : query.split("&")) {
                int idx = pair.indexOf("=");
                if (idx == -1) {
                    continue;
                }
                String key;
                String value;
                try {
                    key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8.name());
                    value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8.name());
                } catch (Exception e) {
                    // 如果解码失败，尝试直接使用未解码的部分作为值
                    key = pair.substring(0, idx);
                    value = pair.substring(idx + 1);
                }
                queryPairs.put(key, value);
            }
            return queryPairs.get(param);
        } catch (Exception e) {
            throw new RuntimeException("从链接中获取参数异常", e);
        }
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
