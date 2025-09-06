package com.finansys.finansysapi.domain.service;

import com.finansys.finansysapi.api.mapper.ReportMapper;
import com.finansys.finansysapi.api.response.ReportResponse;
import com.finansys.finansysapi.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository repository;
    private final ReportMapper reportMapper;

    public List<ReportResponse> findByLastMonths(int monthsBack) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusMonths(monthsBack).withDayOfMonth(1); // primeiro dia do mês alvo

        var expenses = repository.findByDateRange(startDate, now);
        return reportMapper.toReportResponseList(expenses);
    }

}
