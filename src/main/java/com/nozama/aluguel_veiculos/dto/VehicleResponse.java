package com.nozama.aluguel_veiculos.dto;

import com.nozama.aluguel_veiculos.domain.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VehicleResponse(
        String placa,
        BrandResponse brand,
        ModelResponse model,
        Integer ano,
        ColorResponse color,
        String status,
        String descricao,
        CategoryResponse category,
        InsuranceResponse insurance,
        BigDecimal valorDiario
) {

    public static VehicleResponse fromEntity(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getPlaca(),
                new BrandResponse(
                        vehicle.getBrand().getId(),
                        vehicle.getBrand().getNome()
                ),
                new ModelResponse(
                        vehicle.getModel().getId(),
                        vehicle.getModel().getNome()
                ),
                vehicle.getAno(),
                new ColorResponse(
                        vehicle.getColor().getId(),
                        vehicle.getColor().getNome()
                ),
                vehicle.getStatus().getDisplayName(),
                vehicle.getDescricao(),
                new CategoryResponse(
                        vehicle.getCategory().getId(),
                        vehicle.getCategory().getNome(),
                        vehicle.getCategory().getDescricao()
                ),
                new InsuranceResponse(
                        vehicle.getInsurance().getId(),
                        vehicle.getInsurance().getCompany().getName(),
                        vehicle.getInsurance().getValor(),
                        vehicle.getInsurance().getValidade()
                ),
                vehicle.getValorDiario()
        );
    }

    public record BrandResponse(Integer id, String nome) {}
    public record ModelResponse(Integer id, String nome) {}
    public record ColorResponse(Integer id, String nome) {}
    public record CategoryResponse(Integer id, String nome, String descricao) {}
    public record InsuranceResponse(Long id, String empresa, BigDecimal valor, LocalDate validade) {}
}
