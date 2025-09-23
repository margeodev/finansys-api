package com.finansys.finansys_api.domain.service;

import com.finansys.finansys_api.repository.ReportRepository;
import com.finansys.finansys_api.web.mapper.ReportMapper;
import com.finansys.finansys_api.web.response.ReportResponse;
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
