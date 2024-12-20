package com.sli.job.task;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.sli.common.utils.IdUtils;
import com.sli.dao.entity.SliHotSearchDO;
import com.sli.service.SliHotSearchService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.sli.common.enums.HotSearchSource.DOUYIN;

/**
 * @author zkP
 * @date 2024/12/16 22:50
 * @func
 * @description 抖音热搜Java爬虫代码
 */
@Slf4j
@Component
public class DouyinHotSearchJob {

    @Autowired
    private SliHotSearchService sliHotSearchService;

    /**
     * 定时触发爬虫方法，1个小时执行一次
     */
    @XxlJob("douyinHotSearchJob")
    public void hotSearch() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder().url(
                "https://www.iesdouyin.com/web/api/v2/hotsearch/billboard/word/").method("GET", null).build();
        Response response = client.newCall(request).execute();
        JSONObject jsonObject = JSONObject.parseObject(response.body().string());
        JSONArray array = jsonObject.getJSONArray("word_list");

        List<SliHotSearchDO> sliHotSearchDOList = Lists.newArrayList();
        for (int i = 0, len = array.size(); i < len; i++) {
            //获取知乎热搜信息
            JSONObject object = (JSONObject)array.get(i);
            //构建热搜信息榜
            SliHotSearchDO sliHotSearchDO = SliHotSearchDO.builder().hotSearchResource(DOUYIN.getCode()).build();
            //设置文章标题
            sliHotSearchDO.setHotSearchTitle(object.getString("word"));
            //设置抖音三方ID
            sliHotSearchDO.setHotSearchId(IdUtils.getHashId(DOUYIN.getDesc() + sliHotSearchDO.getHotSearchTitle()));
            //设置文章连接
            sliHotSearchDO.setHotSearchUrl(
                    "https://www.douyin.com/search/" + sliHotSearchDO.getHotSearchTitle() + "?type=general");
            //设置热搜热度
            sliHotSearchDO.setHotSearchHeat(object.getString("hot_value"));
            //按顺序排名
            sliHotSearchDO.setHotSearchOrder(i + 1);
            sliHotSearchDOList.add(sliHotSearchDO);
        }
        //数据持久化
        sliHotSearchService.saveCache2DB(sliHotSearchDOList);
    }


}
