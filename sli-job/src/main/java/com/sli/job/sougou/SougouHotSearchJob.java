package com.sli.job.sougou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sli.common.model.dto.HotSearchDetailDTO;
import com.sli.dao.entity.SliHotSearchDO;
import com.sli.service.SliHotSearchService;
import com.sli.service.convert.HotSearchConvert;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.sli.cache.hotSearch.HotSearchCacheManager.CACHE_MAP;
import static com.sli.common.enums.HotSearchEnum.SOUGOU;

/**
 * @author summo
 * @version SougouHotSearchJob.java, 1.0.0
 * @description 搜狗热搜Java爬虫代码
 * @date 2024年08月09
 */
@Component
@Slf4j
public class SougouHotSearchJob {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    @XxlJob("sougouHotSearchJob")
    public ReturnT<String> hotSearch(String param) throws IOException {
        log.info("搜狗热搜爬虫任务开始");
        try {
            //查询搜狗热搜数据
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url("https://go.ie.sogou.com/hot_ranks").method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            JSONArray array = jsonObject.getJSONArray("data");
            List<SliHotSearchDO> sliHotSearchDOList = Lists.newArrayList();
            for (int i = 0, len = array.size(); i < len; i++) {
                //获取知乎热搜信息
                JSONObject object = (JSONObject)array.get(i);
                //构建热搜信息榜
                SliHotSearchDO sliHotSearchDO = SliHotSearchDO.builder().hotSearchResource(SOUGOU.getCode()).build();
                //设置知乎三方ID
                sliHotSearchDO.setHotSearchId(object.getString("id"));
                //设置文章标题
                sliHotSearchDO.setHotSearchTitle(object.getJSONObject("attributes").getString("title"));
                //设置文章连接
                sliHotSearchDO.setHotSearchUrl(
                        "https://www.sogou.com/web?ie=utf8&query=" + sliHotSearchDO.getHotSearchTitle());
                //设置热搜热度
                sliHotSearchDO.setHotSearchHeat(object.getJSONObject("attributes").getString("num"));
                //按顺序排名
                sliHotSearchDO.setHotSearchOrder(i + 1);
                sliHotSearchDOList.add(sliHotSearchDO);
            }
            if (CollectionUtils.isEmpty(sliHotSearchDOList)) {
                return ReturnT.SUCCESS;
            }
            //数据加到缓存中
            CACHE_MAP.put(SOUGOU.getCode(), HotSearchDetailDTO.builder()
                //热搜数据
                .hotSearchDTOList(
                    sliHotSearchDOList.stream().map(HotSearchConvert::toDTOWhenQuery).collect(Collectors.toList()))
                //更新时间
                .updateTime(Calendar.getInstance().getTime()).build());
            //数据持久化
            sliHotSearchService.saveCache2DB(sliHotSearchDOList);
            log.info("搜狗热搜爬虫任务结束");
        } catch (IOException e) {
            log.error("获取搜狗数据异常", e);
        }
        return ReturnT.SUCCESS;
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
