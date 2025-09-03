package com.finansys.finansysapi.domain.repository;

import com.finansys.finansysapi.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface CategoryRepository  extends JpaRepository<Category, Long> {

    List<Category> findByIsActiveTrue();

}
