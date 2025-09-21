package com.finansys.finansys_api.repository;

import com.finansys.finansys_api.domain.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e " +
            "WHERE e.user.id = :userId " +
            "AND MONTH(e.createdAt) = MONTH(CURRENT_DATE) " +
            "AND YEAR(e.createdAt) = YEAR(CURRENT_DATE) " +
            "AND e.isPersonal = false " +
            "AND e.isActive = true")
    List<Expense> findByUserInCurrentMonth(@Param("userId") Long userId);

    @Query("SELECT e FROM Expense e " +
            "WHERE e.user.id = :userId " +
            "AND MONTH(e.createdAt) = MONTH(CURRENT_DATE) " +
            "AND YEAR(e.createdAt) = YEAR(CURRENT_DATE) " +
            "AND e.isPersonal = true " +
            "AND e.isActive = true")
    List<Expense> findMyPersonalExpensesInCurrentMonth(@Param("userId") Long userId);

}
