package com.sli.job.douyin;

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
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sli.cache.hotSearch.HotSearchCacheManager.CACHE_MAP;
import static com.sli.common.enums.HotSearchSource.DOUYIN;

/**
 * @author summo
 * @version DouyinHotSearchJob.java, 1.0.0
 * @description 抖音热搜Java爬虫代码
 * @date 2024年08月09
 */
@Component
@Slf4j
public class DouyinHotSearchJob {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    @XxlJob("douyinHotSearchJob")
    public ReturnT<String> hotSearch(String param) throws IOException {
        log.info("抖音热搜爬虫任务开始");
        try {
            //查询抖音热搜数据
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url("https://www.iesdouyin.com/web/api/v2/hotsearch/billboard/word/").method("GET", null).build();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            JSONArray array = jsonObject.getJSONArray("word_list");
            List<SliHotSearchDO> sliHotSearchDOList = Lists.newArrayList();
            for (int i = 0, len = array.size(); i < len; i++) {
                //获取知乎热搜信息
                JSONObject object = (JSONObject) array.get(i);
                //构建热搜信息榜
                SliHotSearchDO sliHotSearchDO = SliHotSearchDO.builder().hotSearchResource(DOUYIN.getCode()).build();
                //设置文章标题
                sliHotSearchDO.setHotSearchTitle(object.getString("word"));
                //设置知乎三方ID
                sliHotSearchDO.setHotSearchId(getHashId(DOUYIN.getCode() + sliHotSearchDO.getHotSearchTitle()));
                //设置文章连接
                sliHotSearchDO.setHotSearchUrl("https://www.douyin.com/search/" + sliHotSearchDO.getHotSearchTitle() + "?type=general");
                //设置热搜热度
                sliHotSearchDO.setHotSearchHeat(object.getString("hot_value"));
                //按顺序排名
                sliHotSearchDO.setHotSearchOrder(i + 1);
                sliHotSearchDOList.add(sliHotSearchDO);
            }
            if (CollectionUtils.isEmpty(sliHotSearchDOList)) {
                return ReturnT.SUCCESS;
            }
            //数据加到缓存中
            CACHE_MAP.put(DOUYIN.getCode(), HotSearchDetailDTO.builder()
                    //热搜数据
                    .hotSearchDTOList(sliHotSearchDOList.stream().map(HotSearchConvert::toDTOWhenQuery).collect(Collectors.toList()))
                    //更新时间
                    .updateTime(Calendar.getInstance().getTime()).build());
            //数据持久化
            sliHotSearchService.saveCache2DB(sliHotSearchDOList);
            log.info("抖音热搜爬虫任务结束");
        } catch (IOException e) {
            log.error("获取抖音数据异常", e);
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
