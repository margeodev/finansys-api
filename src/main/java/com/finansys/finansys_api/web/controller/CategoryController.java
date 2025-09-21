package com.finansys.finansys_api.web.controller;

import com.finansys.finansys_api.domain.service.CategoryService;
import com.finansys.finansys_api.web.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        List<CategoryResponse> responses =  service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

}
