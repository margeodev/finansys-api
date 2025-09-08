package com.finansys.finansysapi.domain.service;

import com.finansys.finansysapi.api.mapper.ExpenseMapper;
import com.finansys.finansysapi.api.request.ExpenseRequest;
import com.finansys.finansysapi.api.response.ExpenseResponse;
import com.finansys.finansysapi.domain.exception.ExpenseException;
import com.finansys.finansysapi.domain.model.Category;
import com.finansys.finansysapi.domain.model.Expense;
import com.finansys.finansysapi.domain.model.User;
import com.finansys.finansysapi.domain.repository.CategoryRepository;
import com.finansys.finansysapi.domain.repository.ExpenseRepository;
import com.finansys.finansysapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository repository;
    private final ExpenseMapper expenseMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseResponse create(ExpenseRequest request) {
        validateRequest(request);
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setIsActive(true);
        expense.setIsExclusive(request.getIsExclusive());

        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = findUser(userName);
        Category category = findCategoryById(request.getCategoryId());

        expense.setUser(user);
        expense.setCategory(category);

        Expense savedExpense = repository.save(expense);

        return expenseMapper.toExpenseResponse(savedExpense);
    }

    public List<ExpenseResponse> findByUserAndMonth(String userName) {
        User user = findUser(userName);
        List<Expense> expenses = repository.findByUserInCurrentMonth(user.getId());
        return expenseMapper.toExpenseResponseList(expenses);
    }

    public BigDecimal findTotalAmountByUser(String userName) {
        User user = findUser(userName);
        List<Expense> expenses = repository.findByUserInCurrentMonth(user.getId());
        return expenseMapper.calculateTotalAmount(expenses);
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

    public ExpenseResponse makeIsAdvancePayment(Long expenseId) {
        Expense expense = findExpenseById(expenseId);
        expense.setIsAdvancePayment(true);
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

    private User findUser(String userName) {
        return userRepository.findByUsernameAndIsActiveTrue(userName)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    }
}
