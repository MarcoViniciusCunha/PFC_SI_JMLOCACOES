package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.services.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contrato")
@RequiredArgsConstructor
public class PdfController {
    private final RentalRepository rentalRepository;
    private final PdfService pdfService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) {

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locação não encontrada"));

        byte[] pdf = pdfService.gerarContrato(rental);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contrato_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
