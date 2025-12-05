package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.InsuranceCompanyController;
import com.nozama.aluguel_veiculos.domain.InsuranceCompany;
import com.nozama.aluguel_veiculos.dto.InsuranceCompanyRequest;
import com.nozama.aluguel_veiculos.repository.InsuranceCompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InsuranceCompanyControllerTest {

    private MockMvc mockMvc;
    private InsuranceCompanyRepository repository;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(InsuranceCompanyRepository.class);
        InsuranceCompanyController controller = new InsuranceCompanyController(repository);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testCreateNewCompany() throws Exception {
        InsuranceCompanyRequest request = new InsuranceCompanyRequest("Allianz", "1199999999");
        InsuranceCompany saved = new InsuranceCompany(request);

        Mockito.when(repository.findByNameIgnoreCase("Allianz")).thenReturn(Optional.empty());
        Mockito.when(repository.save(any())).thenReturn(saved);

        mockMvc.perform(
                        post("/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "name": "Allianz",
                                  "contact": "1199999999"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Allianz"))
                .andExpect(jsonPath("$.contact").value("1199999999"));
    }

    @Test
    void testCreateExistingCompany() throws Exception {
        InsuranceCompany existing =
                new InsuranceCompany(new InsuranceCompanyRequest("Porto Seguro", "1133333333"));

        Mockito.when(repository.findByNameIgnoreCase("Porto Seguro"))
                .thenReturn(Optional.of(existing));

        mockMvc.perform(
                        post("/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "name": "Porto Seguro",
                                  "contact": "1133333333"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Porto Seguro"))
                .andExpect(jsonPath("$.contact").value("1133333333"));
    }

    @Test
    void testFindAll() throws Exception {
        InsuranceCompany c1 = new InsuranceCompany(new InsuranceCompanyRequest("Allianz", "1111"));
        InsuranceCompany c2 = new InsuranceCompany(new InsuranceCompanyRequest("Porto Seguro", "2222"));

        Mockito.when(repository.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFindByIdFound() throws Exception {
        InsuranceCompany company =
                new InsuranceCompany(new InsuranceCompanyRequest("Sompo", "4444"));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(company));

        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sompo"))
                .andExpect(jsonPath("$.contact").value("4444"));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        Mockito.when(repository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/companies/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSuccess() throws Exception {
        Mockito.when(repository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/companies/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        Mockito.when(repository.existsById(10)).thenReturn(false);

        mockMvc.perform(delete("/companies/10"))
                .andExpect(status().isNotFound());
    }
}
