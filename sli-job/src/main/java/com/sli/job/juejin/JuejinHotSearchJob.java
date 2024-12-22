package com.sli.job.juejin;

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
import static com.sli.common.enums.HotSearchEnum.JUEJIN;

/**
 * @author summo
 * @version JuejinHotSearchJob.java, 1.0.0
 * @description 掘金热搜Java爬虫代码
 * @date 2024年08月09
 */
@Component
@Slf4j
public class JuejinHotSearchJob {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    @XxlJob("juejinHotSearchJob")
    public ReturnT<String> hotSearch(String param) throws IOException {
        log.info("掘金热搜爬虫任务开始");
        try {
            //查询掘金热搜数据
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url(
                    "https://api.juejin.cn/content_api/v1/content/article_rank?category_id=1&type=hot&spider=0").method(
                    "GET", null).build();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            JSONArray array = jsonObject.getJSONArray("data");
            List<SliHotSearchDO> sliHotSearchDOList = Lists.newArrayList();
            for (int i = 0, len = array.size(); i < len; i++) {
                //获取知乎热搜信息
                JSONObject object = (JSONObject)array.get(i);
                //构建热搜信息榜
                SliHotSearchDO sliHotSearchDO = SliHotSearchDO.builder().hotSearchResource(JUEJIN.getCode()).build();
                //设置知乎三方ID
                sliHotSearchDO.setHotSearchId(object.getJSONObject("content").getString("content_id"));
                //设置文章标题
                sliHotSearchDO.setHotSearchTitle(object.getJSONObject("content").getString("title"));
                //设置文章连接
                sliHotSearchDO.setHotSearchUrl("https://juejin.cn/post/" + sliHotSearchDO.getHotSearchId());
                //设置热搜热度
                sliHotSearchDO.setHotSearchHeat(object.getJSONObject("content_counter").getString("view"));
                //设置热搜作者
                sliHotSearchDO.setHotSearchAuthor(object.getJSONObject("author").getString("author"));
                //设置热搜作者头像
                sliHotSearchDO.setHotSearchAuthorAvatar(object.getJSONObject("author").getString("avatar"));

                //按顺序排名
                sliHotSearchDO.setHotSearchOrder(i + 1);
                sliHotSearchDOList.add(sliHotSearchDO);
            }
            if (CollectionUtils.isEmpty(sliHotSearchDOList)) {
                return ReturnT.SUCCESS;
            }
            //数据加到缓存中
            CACHE_MAP.put(JUEJIN.getCode(), HotSearchDetailDTO.builder()
                    //热搜数据
                    .hotSearchDTOList(sliHotSearchDOList.stream().map(HotSearchConvert::toDTOWhenQuery).collect(Collectors.toList()))
                    //更新时间
                    .updateTime(Calendar.getInstance().getTime()).build());
            //数据持久化
            sliHotSearchService.saveCache2DB(sliHotSearchDOList);
            log.info("掘金热搜爬虫任务结束");
        } catch (IOException e) {
            log.error("获取掘金数据异常", e);
        }
        return ReturnT.SUCCESS;
    }


    @PostConstruct
    public void init() {
        // 启动运行爬虫一次
        try {
            hotSearch(null);
        } catch (IOException e) {
            log.error("启动爬虫脚本失败", e);
        }
    }
}
