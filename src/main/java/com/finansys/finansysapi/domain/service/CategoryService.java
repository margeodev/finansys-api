package com.finansys.finansysapi.domain.service;

import com.finansys.finansysapi.api.mapper.CategoryMapper;
import com.finansys.finansysapi.api.response.CategoryResponse;
import com.finansys.finansysapi.domain.model.Category;
import com.finansys.finansysapi.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> findAll() {
        List<Category> categories = repository.findByIsActiveTrue();
        return categoryMapper.toCategoryResponseList(categories);
    }

    public Optional<CategoryResponse> delete(Long id) {
        return repository.findById(id)
                .map(categoryToDel -> {
                    categoryToDel.setIsActive(Boolean.FALSE);
                    repository.save(categoryToDel);
                    return categoryMapper.toCategoryResponse(categoryToDel);
                });
    }
}
