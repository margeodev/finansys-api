package com.finansys.finansys_api.repository;

import com.finansys.finansys_api.domain.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e " +
           "WHERE e.createdAt >= :startDate " +
           "AND e.createdAt <= :endDate " +
           "AND e.isActive = true")
    List<Expense> findByDateRange(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);


}
