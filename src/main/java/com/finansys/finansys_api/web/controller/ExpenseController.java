package com.finansys.finansys_api.web.controller;

import com.finansys.finansys_api.domain.service.ExpenseService;
import com.finansys.finansys_api.web.request.ExpenseRequest;
import com.finansys.finansys_api.web.response.BalanceResponse;
import com.finansys.finansys_api.web.response.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/period")
    public ResponseEntity<List<ExpenseResponse>> findExpensesByMonth(
            @RequestHeader("username") String userName,
            @RequestParam(name = "isPersonal", defaultValue = "false") Boolean isPersonal,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        //Se nenhuma data for fornecida, use a data atual.
        LocalDate targetDate = Optional.ofNullable(date).orElse(LocalDate.now());

        List<ExpenseResponse> responses =  service.getExpensesForMonth(userName, isPersonal, targetDate);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/total")
    public ResponseEntity<BalanceResponse> findTotalAmountByUser(
            @RequestHeader("username") String userName,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        //Se nenhuma data for fornecida, use a data atual.
        LocalDate targetDate = Optional.ofNullable(date).orElse(LocalDate.now());
        BalanceResponse response =  service.findBalanceByUser(userName, targetDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{limit}")
    public ResponseEntity<List<ExpenseResponse>> findLastExpenses(@PathVariable int limit) {
        List<ExpenseResponse> responses = service.getLastExpenses(limit);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/advance-payment")
    public ResponseEntity<ExpenseResponse> changeIsAdvancePayment(@PathVariable Long id, @RequestBody Boolean isAdvancePayment) {
        var expense = service.changeIsAdvancePayment(id, isAdvancePayment);
        return ResponseEntity.ok(expense);
    }

}
