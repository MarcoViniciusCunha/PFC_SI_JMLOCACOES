package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.category.Category;
import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import com.nozama.aluguel_veiculos.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category create(CategoryRequest categoryRequest) {
        if (repository.existsByNome(categoryRequest.nome())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Categoria '" + categoryRequest.nome() + "' já existe!"
            );
        }
        return repository.save(new Category(categoryRequest));
    }

    public Optional<Category> getByName(String name) {
        return repository.findByNome(name);
    }

    public Category putById(int id, String name) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada!"));

        repository.findByNome(name).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria '" + name + "' já existe!");
            }
        });

        category.setNome(name);
        return repository.save(category);
    }

    public void deleteById(int id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada!");
        }
        repository.deleteById(id);
    }
}