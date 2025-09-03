package com.finansys.finansysapi.api.mapper;

import com.finansys.finansysapi.api.response.CategoryResponse;
import com.finansys.finansysapi.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final ModelMapper mapper;

    public Category toCategory(CategoryResponse response) {
        return mapper.map(response, Category.class);
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return mapper.map(category, CategoryResponse.class);
    }

    public List<CategoryResponse> toCategoryResponseList(List<Category> categories) {
        return categories.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }
}
