package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Insurance;
import com.nozama.aluguel_veiculos.domain.InsuranceCompany;
import com.nozama.aluguel_veiculos.dto.InsuranceRequest;
import com.nozama.aluguel_veiculos.repository.InsuranceCompanyRepository;
import com.nozama.aluguel_veiculos.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository repository;
    private final InsuranceCompanyRepository companyRepository;

    public Insurance create(InsuranceRequest request){
        InsuranceCompany company = getCompanyById(request.companyId());
        return repository.save(new Insurance(request, company));
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
            insurance.setCompany(getCompanyById((Integer) updates.get("companyId")));
        }

        if (updates.containsKey("valor")) {
            insurance.setValor(new BigDecimal(updates.get("valor").toString()));
        }

        if (updates.containsKey("validade")) {
            insurance.setValidade(LocalDate.parse(updates.get("validade").toString()));
        }

        return repository.save(insurance);
    }


    public void deleteByID(Long id) {
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado");
        }
        repository.deleteById(id);
    }

    private InsuranceCompany getCompanyById(Integer id){
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguradora não encontrada"));
    }
}
