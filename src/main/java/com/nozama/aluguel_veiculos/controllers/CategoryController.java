package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Category;
import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import com.nozama.aluguel_veiculos.repository.CategoryRepository;
import com.nozama.aluguel_veiculos.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository repository;

    public CategoryController(CategoryService categoryService, CategoryRepository repository) {
        this.categoryService = categoryService;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody @Valid CategoryRequest categoryRequest) {
        Category category = categoryService.create(categoryRequest);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getByName(@PathVariable Integer id) {
        Optional<Category> category = repository.findById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Integer id, @RequestBody @Valid CategoryRequest categoryRequest) {
        Category updated = categoryService.update(id, categoryRequest);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
