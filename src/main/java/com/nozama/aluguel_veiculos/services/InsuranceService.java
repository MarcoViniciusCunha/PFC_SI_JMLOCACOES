package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Insurance;
import com.nozama.aluguel_veiculos.domain.InsuranceCompany;
import com.nozama.aluguel_veiculos.dto.InsuranceRequest;
import com.nozama.aluguel_veiculos.repository.InsuranceCompanyRepository;
import com.nozama.aluguel_veiculos.repository.InsuranceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class InsuranceService {

    private final InsuranceRepository repository;
    private final InsuranceCompanyRepository companyRepository;

    public InsuranceService(InsuranceRepository repository, InsuranceCompanyRepository companyRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
    }

    public Insurance create(InsuranceRequest request){
        InsuranceCompany company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguradora não encontrada"));

        Insurance insurance = new Insurance(request, company);
        return repository.save(insurance);
    }

    public List<Insurance> findAll(){
        return repository.findAll();
    }

    public Insurance getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado"));
    }

    public Insurance update(Long id, Map<String, Object> updates) {
        Insurance insurance = getById(id);

        if (updates.containsKey("companyId")) {
            Integer companyId = (Integer) updates.get("companyId");
            InsuranceCompany company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguradora não encontrada"));
            insurance.setCompany(company);
        }

        if (updates.containsKey("valor")) {
            insurance.setValor(new BigDecimal(updates.get("valor").toString()));
        }

        if (updates.containsKey("validade")) {
            insurance.setValidade(LocalDate.parse((String) updates.get("validade")));
        }

        return repository.save(insurance);
    }

    public void deleteByID(Long id) {
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado");
        }
        repository.deleteById(id);
    }
}
