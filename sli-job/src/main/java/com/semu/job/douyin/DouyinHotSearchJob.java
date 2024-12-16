package com.semu.job.douyin;

import com.alibaba.fastjson2.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zkP
 * @date 2024/12/16 22:50
 * @func
 * @description 抖音热搜Java爬虫代码
 */
@Component
public class DouyinHotSearchJob {

    /**
     * 定时触发爬虫方法，1个小时执行一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void hotSearch() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.iesdouyin.com/web/api/v2/hotsearch/billboard/word/")
                .method("GET", null)
                .addHeader("accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;"
                                + "q=0.8,application/signed-exchange;v=b3;q=0.7")
                .addHeader("accept-language", "zh-CN,zh;q=0.9")
                .addHeader("cache-control", "no-cache")
                .addHeader("cookie",
                        "ttwid=1%7CJ6ehEognyMAob_gD6oZwA40monN8E_sENr3IUZmuk7o%7C1712472728"
                                + "%7C44b0cd0003fb75861789d62e56f014eaea3d198898a0ae9a947bf61d95d8ac1a; "
                                + "__ac_signature=_02B4Z6wo00f01fFoqvgAAIDBFmj97SX8qiXxSK5AABr708; "
                                + "__ac_referer=https://pre-dc-console.alibaba-inc.com/; "
                                + "ttwid=1%7CX9ppA_NoTHJI9DG3JN7wNnZ662r-aJbZwCFPLLGK-og%7C1713836331"
                                + "%7Cdbc79a439d0ecc994f60043d66b4ad3ff81c3820f3ab83ef85d30875cc59a18b")
                .addHeader("pragma", "no-cache")
                .addHeader("priority", "u=0, i")
                .addHeader("sec-ch-ua", "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "none")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
                                + "Chrome/126.0.0.0 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(JSONObject.toJSONString(response.body().string()));
    }
}
