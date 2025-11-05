package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Category;
import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import com.nozama.aluguel_veiculos.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category create(CategoryRequest request) {
        if (repository.existsByNome(request.nome())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Categoria '" + request.nome() + "' já existe!"
            );
        }
        return repository.save(new Category(request));
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category patchById(int id, CategoryRequest request) {
        Category category = findById(id);

        if (request.nome() != null && !request.nome().isBlank()) {
            repository.findByNome(request.nome()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Categoria '" + request.nome() + "' já existe!");
                }
            });
            category.setNome(request.nome());
        }

        if (request.descricao() != null) {
            category.setDescricao(request.descricao());
        }

        return repository.save(category);
    }

    public Category findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada!"));
    }

    public void deleteById(int id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada!");
        }
        repository.deleteById(id);
    }
}