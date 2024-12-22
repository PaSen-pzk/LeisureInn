package com.sli.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sli.common.model.dto.PoetryDTO;
import com.sli.service.PoetryService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PoetryServiceImpl implements PoetryService {

    @Value("${poetry.api.token}")
    private String poetryApiToken;

    @Override
    public PoetryDTO queryPoetry() {
        PoetryDTO poetryDTO = PoetryDTO.builder().build();
        try {
            //查询B站热搜数据
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url("https://v2.jinrishici.com/sentence").addHeader("X-User-Token",
                    poetryApiToken).method("GET", null).build();
            Response response = client.newCall(request).execute();
            JSONObject object = JSONObject.parseObject(response.body().string());
            poetryDTO.setAuthor(object.getJSONObject("data").getJSONObject("origin").getString("author"));
            poetryDTO.setTitle(object.getJSONObject("data").getJSONObject("origin").getString("title"));
            poetryDTO.setDynasty(object.getJSONObject("data").getJSONObject("origin").getString("dynasty"));
            poetryDTO.setContent(object.getJSONObject("data").getJSONObject("origin").getString("content"));
            poetryDTO.setExcerpt(object.getJSONObject("data").getString("content"));
        } catch (Exception e) {
            log.error("查询诗词信息异常", e);
        }
        return poetryDTO;
    }
}
