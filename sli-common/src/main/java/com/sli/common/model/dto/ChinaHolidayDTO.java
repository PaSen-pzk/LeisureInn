package com.sli.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChinaHolidayDTO {

  /**
   * 日期
   */
  private String date;

  /**
   * 节假日
   */
  private String holiday;

  /**
   * 是否放假
   */
  private String holidayRecess;
}
