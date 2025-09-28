package com.finansys.finansys_api.domain.service;

import com.finansys.finansys_api.domain.model.Category;
import com.finansys.finansys_api.domain.model.Expense;
import com.finansys.finansys_api.domain.model.User;
import com.finansys.finansys_api.exception.ExpenseException;
import com.finansys.finansys_api.repository.CategoryRepository;
import com.finansys.finansys_api.repository.ExpenseRepository;
import com.finansys.finansys_api.web.mapper.ExpenseMapper;
import com.finansys.finansys_api.web.request.ExpenseRequest;
import com.finansys.finansys_api.web.response.BalanceResponse;
import com.finansys.finansys_api.web.response.ExpenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository repository;
    private final ExpenseMapper expenseMapper;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    public ExpenseResponse create(ExpenseRequest request) {
        validateRequest(request);
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setIsActive(true);
        expense.setIsPersonal(request.getIsPersonal() != null && request.getIsPersonal());

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserName(userName);
        Category category = findCategoryById(request.getCategoryId());

        expense.setUser(user);
        expense.setCategory(category);

        Expense savedExpense = repository.save(expense);

        return expenseMapper.toExpenseResponse(savedExpense);
    }

    public BalanceResponse findBalanceByUser(String userName, LocalDate date) {
        List<Expense> expenses = getExpenses(userName, false, date);
        return expenseMapper.toBalanceResponse(expenses);
    }

    public List<ExpenseResponse> getExpensesForMonth(String userName, Boolean isPersonal, LocalDate date) {
        List<Expense> expenses = getExpenses(userName, isPersonal, date);
        return expenseMapper.toExpenseResponseList(expenses);
    }

    public ExpenseResponse changeIsAdvancePayment(Long expenseId, Boolean isAdvancePayment) {
        Expense expense = findExpenseById(expenseId);
        expense.setIsAdvancePayment(isAdvancePayment);
        Expense updatedExpense = repository.save(expense);
        return expenseMapper.toExpenseResponse(updatedExpense);
    }

    private List<Expense> getExpenses(String userName, Boolean isPersonal, LocalDate date) {
        User user = userService.findByUserName(userName);
        LocalDate startOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        return repository.findExpensesByUserAndDateRange(user.getId(), isPersonal, startOfMonth, endOfMonth);
    }

    private void validateRequest(ExpenseRequest request) {
        // Usando StringUtils.hasText para verificar se não é nulo, vazio ou só com espaços
        if (!StringUtils.hasText(request.getDescription())) {
            throw new ExpenseException("A descrição é obrigatória e não pode estar em branco.");
        }
        if (request.getAmount() == null) {
            throw new ExpenseException("O valor (amount) é obrigatório.");
        }
        if (request.getCategoryId() == null) {
            throw new ExpenseException("O ID da categoria (categoryId) é obrigatório.");
        }
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    private Expense findExpenseById(Long expenseId) {
        return repository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Lançamento não encontrado"));
    }

}
