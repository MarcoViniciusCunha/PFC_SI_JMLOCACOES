package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.dto.MarkRequest;
import com.nozama.aluguel_veiculos.repository.MarkRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nozama.aluguel_veiculos.domain.Mark;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mark")
public class MarkController {

    private final MarkRepository repository;

    public MarkController(MarkRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Mark> create(@RequestBody @Valid MarkRequest request) {
        Mark mark = new Mark(request);
        Mark saved = repository.save(mark);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Mark>> findAll() {
        List<Mark> marks = repository.findAll();
        return ResponseEntity.ok().body(marks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mark> findById(@PathVariable Integer id) {
        Optional<Mark> existing = repository.findById(id);
        if (existing.isPresent()) {
            return ResponseEntity.ok().body(existing.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mark> update(@PathVariable Integer id, @RequestBody Mark mark) {
        Optional<Mark> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            Mark existing = existingOpt.get();
            existing.setNome(mark.getNome());
            Mark saved = repository.save(existing);
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer  id) {
        Optional<Mark> existing = repository.findById(id);
        if (existing.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
