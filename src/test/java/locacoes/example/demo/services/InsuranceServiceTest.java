package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.Insurance;
import com.nozama.aluguel_veiculos.domain.InsuranceCompany;
import com.nozama.aluguel_veiculos.dto.InsuranceRequest;
import com.nozama.aluguel_veiculos.repository.InsuranceCompanyRepository;
import com.nozama.aluguel_veiculos.repository.InsuranceRepository;
import com.nozama.aluguel_veiculos.services.InsuranceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsuranceServiceTest {

    @Mock
    private InsuranceRepository insuranceRepository;

    @Mock
    private InsuranceCompanyRepository companyRepository;

    @InjectMocks
    private InsuranceService insuranceService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInsurance() {
        InsuranceCompany company = new InsuranceCompany();
        company.setId(1);
        company.setName("Porto Seguro");

        InsuranceRequest request =
                new InsuranceRequest(1, new BigDecimal("500.00"), LocalDate.now().plusYears(1));

        Insurance insurance = new Insurance(request, company);
        insurance.setId(1L); // <- IMPORTANTE

        when(companyRepository.findById(1)).thenReturn(Optional.of(company));
        when(insuranceRepository.save(any(Insurance.class))).thenReturn(insurance);

        Insurance created = insuranceService.create(request);

        assertNotNull(created.getId());
        assertEquals(company, created.getCompany());
        assertEquals(new BigDecimal("500.00"), created.getValor());
    }

    @Test
    void testFindAllInsurance() {
        InsuranceCompany company = new InsuranceCompany();
        company.setId(1);

        Insurance i1 = new Insurance(
                new InsuranceRequest(1, new BigDecimal("400.00"), LocalDate.now().plusYears(1)),
                company
        );
        i1.setId(1L);

        Insurance i2 = new Insurance(
                new InsuranceRequest(1, new BigDecimal("600.00"), LocalDate.now().plusYears(2)),
                company
        );
        i2.setId(2L);

        List<Insurance> insurances = List.of(i1, i2);

        when(insuranceRepository.findAll()).thenReturn(insurances);

        List<Insurance> result = insuranceService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void testGetByIdFound() {
        InsuranceCompany company = new InsuranceCompany();
        company.setId(1);

        Insurance insurance =
                new Insurance(new InsuranceRequest(1, new BigDecimal("300.00"), LocalDate.now().plusYears(1)), company);
        insurance.setId(1L);

        when(insuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));

        Insurance result = insuranceService.getById(1L);

        assertEquals(new BigDecimal("300.00"), result.getValor());
        assertNotNull(result.getCompany());
    }

    @Test
    void testGetByIdNotFound() {
        when(insuranceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> insuranceService.getById(999L));
    }

    @Test
    void testUpdateInsurance() {
        InsuranceCompany company = new InsuranceCompany();
        company.setId(1);

        Insurance insurance =
                new Insurance(new InsuranceRequest(1, new BigDecimal("200.00"), LocalDate.now()), company);
        insurance.setId(1L);

        when(insuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));
        when(insuranceRepository.save(any(Insurance.class))).thenAnswer(i -> i.getArguments()[0]);

        Map<String, Object> updates = new HashMap<>();
        updates.put("valor", "999.99");
        updates.put("validade", LocalDate.now().plusYears(2).toString());

        Insurance updated = insuranceService.update(1L, updates);

        assertEquals(new BigDecimal("999.99"), updated.getValor());
        assertEquals(LocalDate.now().plusYears(2), updated.getValidade());
    }

    @Test
    void testDeleteInsuranceSuccess() {
        when(insuranceRepository.existsById(1L)).thenReturn(true);

        insuranceService.deleteByID(1L);

        verify(insuranceRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteInsuranceNotFound() {
        when(insuranceRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResponseStatusException.class,
                () -> insuranceService.deleteByID(999L));
    }
}
