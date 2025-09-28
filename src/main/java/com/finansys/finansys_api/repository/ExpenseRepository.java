package com.finansys.finansys_api.repository;

import com.finansys.finansys_api.domain.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e " +
            "WHERE e.user.id = :userId " +
            "AND e.createdAt BETWEEN :startOfMonth AND :endOfMonth " +
            "AND e.isPersonal = :isPersonal " +
            "AND e.isActive = true")
    List<Expense> findExpensesByUserAndDateRange(@Param("userId") Long userId,
                                          @Param("isPersonal") Boolean isPersonal,
                                          @Param("startOfMonth") LocalDate startOfMonth,
                                          @Param("endOfMonth") LocalDate endOfMonth);

}
