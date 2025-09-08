package com.finansys.finansysapi.api.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseRequest {
    private String description;
    private BigDecimal amount;
    private Long categoryId;
    private Boolean isPersonal;
}

