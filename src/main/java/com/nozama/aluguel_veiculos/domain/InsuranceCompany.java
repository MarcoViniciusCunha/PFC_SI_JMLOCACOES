package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.InsuranceCompanyRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "insurance_company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String contact;

    public InsuranceCompany(InsuranceCompanyRequest request) {
        this.name = request.name();
        this.contact = request.contact();
    }
}
