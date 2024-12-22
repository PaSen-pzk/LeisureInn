package com.sli.service.impl;

import com.sli.common.model.dto.SentenceDTO;
import com.sli.service.SentenceService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SentenceServiceImpl implements SentenceService {

    @Override
    public SentenceDTO querySentence() {
        SentenceDTO sentenceDTO = SentenceDTO.builder().build();
        try {
            //查询B站热搜数据
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder().url("https://api.oick.cn/yiyan/api.php").method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            sentenceDTO.setSentence(response.body().string());
        } catch (Exception e) {
            log.error("查询诗词信息异常", e);
        }
        return sentenceDTO;
    }
}
