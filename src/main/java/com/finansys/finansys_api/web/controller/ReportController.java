package com.finansys.finansys_api.web.controller;

import com.finansys.finansys_api.domain.service.ReportService;
import com.finansys.finansys_api.web.response.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService service;

    @GetMapping
    public ResponseEntity<List<ReportResponse>> findAll(@RequestParam int monthsBack) {
        List<ReportResponse> responses =  service.findByLastMonths(monthsBack);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
