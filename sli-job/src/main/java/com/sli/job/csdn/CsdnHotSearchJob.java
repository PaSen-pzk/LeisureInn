package com.sli.job.csdn;

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
import static com.sli.common.enums.HotSearchEnum.CSDN;

/**
 * @author summo
 * @version CsdnHotSearchJob.java, 1.0.0
 * @description Csdn热搜Java爬虫代码
 * @date 2024年08月09
 */
@Component
@Slf4j
public class CsdnHotSearchJob {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    @XxlJob("csdnHotSearchJob")
    public ReturnT<String> hotSearch(String param) throws IOException {
        log.info("Csdn热搜爬虫任务开始");
        try {
            //查询CSDN热搜数据
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url(
                    "https://blog.csdn.net/phoenix/web/blog/hot-rank?page=0&pageSize=25").addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0"
                            + ".3770.142 Safari/537.36").method("GET", null).build();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            JSONArray array = jsonObject.getJSONArray("data");
            List<SliHotSearchDO> sliHotSearchDOList = Lists.newArrayList();
            for (int i = 0, len = array.size(); i < len; i++) {
                //获取知乎热搜信息
                JSONObject object = (JSONObject)array.get(i);
                //构建热搜信息榜
                SliHotSearchDO sliHotSearchDO = SliHotSearchDO.builder().hotSearchResource(CSDN.getCode()).build();
                //设置知乎三方ID
                sliHotSearchDO.setHotSearchId(object.getString("productId"));
                //设置文章标题
                sliHotSearchDO.setHotSearchTitle(object.getString("articleTitle"));
                //设置文章连接
                sliHotSearchDO.setHotSearchUrl(object.getString("articleDetailUrl"));
                //设置热搜热度
                sliHotSearchDO.setHotSearchHeat(object.getString("hotRankScore"));
                //设置热搜作者
                sliHotSearchDO.setHotSearchAuthor(object.getString("nickName"));
                //设置热搜作者头像
                sliHotSearchDO.setHotSearchAuthorAvatar(object.getString("avatarUrl"));

                //按顺序排名
                sliHotSearchDO.setHotSearchOrder(i + 1);
                sliHotSearchDOList.add(sliHotSearchDO);
            }
            if (CollectionUtils.isEmpty(sliHotSearchDOList)) {
                return ReturnT.SUCCESS;
            }
            //数据加到缓存中
            CACHE_MAP.put(CSDN.getCode(), HotSearchDetailDTO.builder()
                    //热搜数据
                    .hotSearchDTOList(sliHotSearchDOList.stream().map(HotSearchConvert::toDTOWhenQuery).collect(Collectors.toList()))
                    //更新时间
                    .updateTime(Calendar.getInstance().getTime()).build());
            //数据持久化
            sliHotSearchService.saveCache2DB(sliHotSearchDOList);
            log.info("Csdn热搜爬虫任务结束");
        } catch (IOException e) {
            log.error("获取Csdn数据异常", e);
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
