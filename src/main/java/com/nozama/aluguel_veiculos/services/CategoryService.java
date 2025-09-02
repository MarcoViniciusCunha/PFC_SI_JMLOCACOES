package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.category.Category;
import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import com.nozama.aluguel_veiculos.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category create(CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest);
        return repository.save(category);
    }
}
