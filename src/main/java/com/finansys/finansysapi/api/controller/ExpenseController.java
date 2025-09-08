package com.finansys.finansysapi.api.controller;

import com.finansys.finansysapi.api.request.ExpenseRequest;
import com.finansys.finansysapi.api.response.ExpenseResponse;
import com.finansys.finansysapi.domain.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/current-month/personal")
    public ResponseEntity<List<ExpenseResponse>> findMyPersonalExpensesInCurrentMonth(@RequestHeader("username") String userName) {
        List<ExpenseResponse> responses =  service.findMyPersonalExpensesInCurrentMonth(userName);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> findTotalAmountByUser(@RequestHeader("username") String userName) {
        BigDecimal response =  service.findTotalAmountByUser(userName);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> editExpense(@PathVariable Long id, @RequestBody ExpenseRequest request) {
        var expense = service.editExpense(id, request);
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/{id}/advance-payment")
    public ResponseEntity<ExpenseResponse> makeIsAdvancePayment(@PathVariable Long id) {
        var expense = service.makeIsAdvancePayment(id);
        return ResponseEntity.ok(expense);
    }

}
