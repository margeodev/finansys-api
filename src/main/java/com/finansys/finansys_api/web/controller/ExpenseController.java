package com.finansys.finansys_api.web.controller;

import com.finansys.finansys_api.domain.service.ExpenseService;
import com.finansys.finansys_api.web.request.ExpenseRequest;
import com.finansys.finansys_api.web.response.BalanceResponse;
import com.finansys.finansys_api.web.response.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
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
    public ResponseEntity<BalanceResponse> findTotalAmountByUser(@RequestHeader("username") String userName) {
        BalanceResponse response =  service.findBalanceByUser(userName);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/advance-payment")
    public ResponseEntity<ExpenseResponse> changeIsAdvancePayment(@PathVariable Long id, @RequestBody Boolean isAdvancePayment) {
        var expense = service.changeIsAdvancePayment(id, isAdvancePayment);
        return ResponseEntity.ok(expense);
    }

}
