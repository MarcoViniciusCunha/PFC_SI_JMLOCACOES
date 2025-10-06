package com.nozama.aluguel_veiculos.specification;

import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.dto.VehicleFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class VehicleSpecification {

    public static Specification<Vehicle> filter(VehicleFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getPlaca() != null) {
                predicates.add(cb.equal(root.get("placa"), filter.getPlaca()));
            }
            if (filter.getIdMarca() != null) {
                predicates.add(cb.equal(root.get("brand").get("id"), filter.getIdMarca()));
            }
            if (filter.getIdModelo() != null) {
                predicates.add(cb.equal(root.get("model").get("id"), filter.getIdModelo()));
            }
            if (filter.getAno() != null) {
                predicates.add(cb.equal(root.get("ano"), filter.getAno()));
            }
            if (filter.getIdCor() != null) {
                predicates.add(cb.equal(root.get("color").get("id"), filter.getIdCor()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getIdCategoria() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filter.getIdCategoria()));
            }
            if (filter.getIdSeguro() != null) {
                predicates.add(cb.equal(root.get("insurance").get("id"), filter.getIdSeguro()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
