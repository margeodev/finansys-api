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

import java.util.List;
import java.util.Optional;

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

    public List<ExpenseResponse> findByUserAndMonth(String userName) {
        User user = userService.findByUserName(userName);
        List<Expense> expenses = repository.findByUserInCurrentMonth(user.getId());
        return expenseMapper.toExpenseResponseList(expenses);
    }

    public List<ExpenseResponse> findMyPersonalExpensesInCurrentMonth(String userName) {
        User user = userService.findByUserName(userName);
        List<Expense> expenses = repository.findMyPersonalExpensesInCurrentMonth(user.getId());
        return expenseMapper.toExpenseResponseList(expenses);
    }

    public BalanceResponse findBalanceByUser(String userName) {
        User user = userService.findByUserName(userName);
        List<Expense> expenses = repository.findByUserInCurrentMonth(user.getId());
        return expenseMapper.toBalanceResponse(expenses);
    }

    public ExpenseResponse editExpense(Long expenseId, ExpenseRequest request) {
        Expense expense = findExpenseById(expenseId);

        Category category = findCategoryById(request.getCategoryId());

        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(category);
        Expense updatedExpense = repository.save(expense);
        return expenseMapper.toExpenseResponse(updatedExpense);
    }

    public ExpenseResponse changeIsAdvancePayment(Long expenseId, Boolean isAdvancePayment) {
        Expense expense = findExpenseById(expenseId);
        expense.setIsAdvancePayment(isAdvancePayment);
        Expense updatedExpense = repository.save(expense);
        return expenseMapper.toExpenseResponse(updatedExpense);
    }

    public Optional<ExpenseResponse> delete(Long id) {
        return repository.findById(id)
                .map(expenseToDel -> {
                    expenseToDel.setIsActive(Boolean.FALSE);
                    repository.save(expenseToDel);
                    return expenseMapper.toExpenseResponse(expenseToDel);
                });
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
