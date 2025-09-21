package com.finansys.finansys_api.web.mapper;

import com.finansys.finansys_api.domain.model.Category;
import com.finansys.finansys_api.domain.model.Expense;
import com.finansys.finansys_api.web.response.BalanceResponse;
import com.finansys.finansys_api.web.response.CategoryResponse;
import com.finansys.finansys_api.web.response.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log
public class ExpenseMapper {

    private final ModelMapper mapper;

    public ExpenseResponse toExpenseResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount().toString()); // se for BigDecimal
        response.setCategory(buildCategoryResponse(expense.getCategory()));
        response.setDate(expense.getCreatedAt().toString());
        response.setPersonal(expense.getIsPersonal());
        response.setAdvancePayment(expense.getIsAdvancePayment());
        return response;
    }


    public List<ExpenseResponse> toExpenseResponseList(List<Expense> categories) {
        return categories.stream()
                .map(this::toExpenseResponse)
                .collect(Collectors.toList());
    }

    public BalanceResponse toBalanceResponse(List<Expense> expenses) {
        BigDecimal subTotal = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("Subtotal inicial: " + subTotal);

        BigDecimal totalAdvance = expenses.stream()
                .filter(Expense::getIsAdvancePayment)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Divide o totalAdvance por 2
        BigDecimal halfAdvance = totalAdvance.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        log.info("Total advance: " + halfAdvance);

        // Subtrai metade do totalAdvance do subTotal
        BigDecimal adjustedSubTotal = subTotal.subtract(halfAdvance);
        log.info("Subtotal final (ajustado): " + adjustedSubTotal);

        BalanceResponse response = new BalanceResponse();
        response.setSubTotalBalance(adjustedSubTotal); // já com a subtração aplicada
        response.setTotalAdvanceBalance(totalAdvance); // mantém o valor original

        return response;
    }

    private CategoryResponse buildCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .description(category.getDescription())
                .build();
    }
}
