package com.finansys.finansys_api.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseResponse {
    private Long id;
    private String description;
    private CategoryResponse category;
    private String amount;
    private String date;
    private boolean isPersonal;
    private boolean isAdvancePayment;
}
