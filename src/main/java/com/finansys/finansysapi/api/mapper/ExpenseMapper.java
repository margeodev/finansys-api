package com.finansys.finansysapi.api.mapper;

import com.finansys.finansysapi.api.response.BalanceResponse;
import com.finansys.finansysapi.api.response.CategoryResponse;
import com.finansys.finansysapi.api.response.ExpenseResponse;
import com.finansys.finansysapi.domain.model.Category;
import com.finansys.finansysapi.domain.model.Expense;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
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

        BigDecimal totalAdvance = expenses.stream()
                .filter(Expense::getIsAdvancePayment)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BalanceResponse response = new BalanceResponse();
        response.setSubTotalBalance(subTotal);
        response.setTotalAdvanceBalance(totalAdvance);

        return response;
    }



    private CategoryResponse buildCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .description(category.getDescription())
                .build();
    }
}
