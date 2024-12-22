package com.sli.service;

import com.sli.common.model.dto.ChinaHolidayDTO;

import java.util.List;

public interface ChinaHolidayService {
  List<ChinaHolidayDTO> queryHoliday(String month);
}
