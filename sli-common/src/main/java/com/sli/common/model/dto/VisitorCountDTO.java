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
public class VisitorCountDTO {

    private Integer todayPv;

    private Integer todayUv;

    private Integer allPv;

    private Integer allUv;
}
