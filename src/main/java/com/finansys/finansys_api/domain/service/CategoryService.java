package com.finansys.finansys_api.domain.service;

import com.finansys.finansys_api.domain.model.Category;
import com.finansys.finansys_api.repository.CategoryRepository;
import com.finansys.finansys_api.web.mapper.CategoryMapper;
import com.finansys.finansys_api.web.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> findAll() {
        List<Category> categories = repository.findByIsActiveTrue();
        return categoryMapper.toCategoryResponseList(categories);
    }

}
