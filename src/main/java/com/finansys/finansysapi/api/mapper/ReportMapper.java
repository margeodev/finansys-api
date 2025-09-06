package com.finansys.finansysapi.api.mapper;

import com.finansys.finansysapi.api.response.ReportResponse;
import com.finansys.finansysapi.domain.model.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReportMapper {

    public List<ReportResponse> toReportResponseList(List<Expense> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> new AbstractMap.SimpleEntry<>(e.getCategory().getDescription(), e.getCreatedAt().getMonth()),
                        Collectors.mapping(Expense::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet().stream()
                .map(entry -> {
                    ReportResponse response = new ReportResponse();
                    response.setCategoryDescription(entry.getKey().getKey());
                    response.setMonth(entry.getKey().getValue());
                    response.setTotal(entry.getValue());
                    return response;
                })
                .collect(Collectors.toList());
    }
}

