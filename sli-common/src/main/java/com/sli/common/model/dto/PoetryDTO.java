package com.sli.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PoetryDTO {

  private String title;

  private String author;

  private String excerpt;

  private String dynasty;

  private String content;
}
