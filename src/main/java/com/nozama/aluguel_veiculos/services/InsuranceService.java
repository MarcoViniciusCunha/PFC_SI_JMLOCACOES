package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.insurence.Insurance;
import com.nozama.aluguel_veiculos.dto.InsuranceRequest;
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

    public InsuranceService(InsuranceRepository repository) {
        this.repository = repository;
    }

    public Insurance create(InsuranceRequest request){
        Insurance insurance = new Insurance();
        insurance.setEmpresa(request.empresa());
        insurance.setValor(request.valor());
        insurance.setValidade(request.validade());
        return repository.save(insurance);
    }

    public List<Insurance> findAll(){
        return repository.findAll();
    }

    public Insurance getById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado"));
    }

    public Insurance update(int id, Map<String, Object> updates) {
        Insurance insurance = getById(id);

        if (updates.containsKey("empresa")) {
            insurance.setEmpresa((String) updates.get("empresa"));
        }
        if (updates.containsKey("valor")) {
            insurance.setValor(new BigDecimal(updates.get("valor").toString()));
        }
        if (updates.containsKey("validade")) {
            insurance.setValidade(LocalDate.parse((String) updates.get("validade")));
        }

        return repository.save(insurance);
    }


    public void deleteByID(int id) {
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado");
        }
        repository.deleteById(id);
    }

}
