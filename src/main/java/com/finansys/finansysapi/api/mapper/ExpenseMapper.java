package com.finansys.finansysapi.api.mapper;

import com.finansys.finansysapi.api.request.ExpenseRequest;
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
        return response;
    }


    public List<ExpenseResponse> toExpenseResponseList(List<Expense> categories) {
        return categories.stream()
                .map(this::toExpenseResponse)
                .collect(Collectors.toList());
    }

    public BigDecimal calculateTotalAmount(List<Expense> expenses) {
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private CategoryResponse buildCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .description(category.getDescription())
                .build();
    }
}
