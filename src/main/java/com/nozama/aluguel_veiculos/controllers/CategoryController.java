package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.category.Category;
import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import com.nozama.aluguel_veiculos.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @GetMapping("/search")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/search/{nome}")
    public ResponseEntity<Category> getByName(@PathVariable String nome) {
        return categoryService.getByName(nome)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada!"));
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
