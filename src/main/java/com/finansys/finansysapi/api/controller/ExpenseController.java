package com.finansys.finansysapi.api.controller;

import com.finansys.finansysapi.api.request.ExpenseRequest;
import com.finansys.finansysapi.api.response.ExpenseResponse;
import com.finansys.finansysapi.domain.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/entries")
public class ExpenseController {

    private final ExpenseService service;

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@RequestBody ExpenseRequest request) {
        ExpenseResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/current-month")
    public ResponseEntity<List<ExpenseResponse>> findMyExpensesInCurrentMonth(@RequestHeader("username") String userName) {
        List<ExpenseResponse> responses =  service.findByUserAndMonth(userName);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> editExpense(@PathVariable Long id, @RequestBody ExpenseRequest request) {
        var expense = service.editExpense(id, request);
        return ResponseEntity.ok(expense);
    }

}
