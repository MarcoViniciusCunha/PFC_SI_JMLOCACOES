package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.category.Category;
import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import com.nozama.aluguel_veiculos.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<Category> create(@RequestBody @Valid CategoryRequest categoryRequest) {
        Category category = categoryService.create(categoryRequest);
        return ResponseEntity.ok().body(category);
    }

    @PostMapping("/search")
    public ResponseEntity<Category> getByName(@RequestBody @Valid CategoryRequest categoryRequest) {
        var category = categoryService.getByName(categoryRequest.nome());

        if (category.isPresent()) {
            return ResponseEntity.ok().body(category.get());
        }

        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Integer id,  @RequestBody @Valid CategoryRequest categoryRequest) {
        Category update = categoryService.putById(id, categoryRequest.nome());
        return ResponseEntity.ok().body(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
