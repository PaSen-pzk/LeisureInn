package com.sli.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sli.common.model.dto.ChinaHolidayDTO;
import com.sli.common.util.CacheRedisUtil;
import com.sli.service.ChinaHolidayService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class ChinaHolidayServiceImpl implements ChinaHolidayService {

  @Value("${china.holiday.key}")
  private String key;

  /**
   * 底层缓存组件，可以使用ConcurrentMap也可以使用Redis，推荐使用Redis
   */
  private static ConcurrentMap<String, List<ChinaHolidayDTO>> CACHE = Maps.newConcurrentMap();

  @Override
  public List<ChinaHolidayDTO> queryHoliday(String month) {
    return CacheRedisUtil.selectCacheByTemplate(() -> queryByCache(month), () -> queryByApi(month));
  }

  public List<ChinaHolidayDTO> queryByCache(String month) {
    return CACHE.get(month);
  }

  public List<ChinaHolidayDTO> queryByApi(String month) {
    List<ChinaHolidayDTO> chinaHolidayDTOS = Lists.newArrayList();
    try {
      OkHttpClient client = new OkHttpClient().newBuilder().build();
      Request request = new Request.Builder().url(
        "https://api.apihubs.cn/holiday/get?api_key=" + key + "&cn=1&month=" + month).method("GET", null).build();
      Response response = client.newCall(request).execute();
      JSONObject jsonObject = JSONObject.parseObject(response.body().string());
      JSONArray dateArray = jsonObject.getJSONObject("data").getJSONArray("list");
      dateArray.forEach(temp -> {
        JSONObject date = (JSONObject)temp;
        if (!StringUtils.equals("非节假日", date.getString("holiday_cn"))) {
          ChinaHolidayDTO chinaHolidayDTO = ChinaHolidayDTO.builder().date(date.getString("date")).holiday(
            date.getString("holiday_cn")).build();
          if (StringUtils.equals("假期节假日", date.getString("holiday_recess_cn"))) {
            chinaHolidayDTO.setHolidayRecess(date.getString("holiday_recess_cn"));
          }
          chinaHolidayDTOS.add(chinaHolidayDTO);
        }
      });
      CACHE.put(month, chinaHolidayDTOS);
    } catch (IOException e) {
      log.error("获取节假日数据异常", e);
    }
    return chinaHolidayDTOS;
  }
}
