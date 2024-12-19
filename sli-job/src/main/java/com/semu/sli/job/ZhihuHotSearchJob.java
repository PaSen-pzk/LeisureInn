package com.semu.sli.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.semu.sli.dao.entity.SliHotSearchDO;
import com.semu.sli.service.SliHotSearchService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.semu.sli.common.enums.HotSearchSource.ZHIHU;

/**
 * @author zkP
 * @date 2024/12/16 22:50
 * @func
 * @description 知乎热搜Java爬虫代码
 */
@Component
@Slf4j
public class ZhihuHotSearchJob {
    
    @Autowired
    private SliHotSearchService sliHotSearchService;

    /**
     * 定时触发爬虫方法，1个小时执行一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void hotSearch() throws IOException {
        try {
            //查询知乎热搜数据
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url("https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total")
                    .method("GET", null).build();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            JSONArray array = jsonObject.getJSONArray("data");
            List<SliHotSearchDO> sliHotSearchDOList = Lists.newArrayList();
            for (int i = 0, len = array.size(); i < len; i++) {
                //获取知乎热搜信息
                JSONObject object = (JSONObject)array.get(i);
                JSONObject target = object.getJSONObject("target");
                //构建热搜信息榜
                SliHotSearchDO sliHotSearchDO = SliHotSearchDO.builder().hotSearchResource(ZHIHU.getCode()).build();
                //设置知乎三方ID
                sliHotSearchDO.setHotSearchId(target.getString("id"));
                //设置文章连接
                sliHotSearchDO.setHotSearchUrl("https://www.zhihu.com/question/" + sliHotSearchDO.getHotSearchId());
                //设置文章标题
                sliHotSearchDO.setHotSearchTitle(target.getString("title"));
                //设置作者名称
                sliHotSearchDO.setHotSearchAuthor(target.getJSONObject("author").getString("name"));
                //设置作者头像
                sliHotSearchDO.setHotSearchAuthorAvatar(target.getJSONObject("author").getString("avatar_url"));
                //设置文章摘要
                sliHotSearchDO.setHotSearchExcerpt(target.getString("excerpt"));
                //设置热搜热度
                sliHotSearchDO.setHotSearchHeat(object.getString("detail_text").replace("热度", ""));
                //按顺序排名
                sliHotSearchDO.setHotSearchOrder(i + 1);
                sliHotSearchDOList.add(sliHotSearchDO);
            }
            //数据持久化
            sliHotSearchService.saveCache2DB(sliHotSearchDOList);
        } catch (IOException e) {
            log.error("获取知乎数据异常", e);
        }
    }
}
