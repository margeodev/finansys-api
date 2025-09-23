package com.finansys.finansys_api.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Month;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponse {
    private String categoryDescription;
    private Month month;
    private BigDecimal total;
}
