package com.sli.common.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WordCloudDTO {

    private String word;
    private Integer rate;
}
