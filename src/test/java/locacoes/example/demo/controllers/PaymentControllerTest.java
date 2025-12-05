package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.PaymentController;
import com.nozama.aluguel_veiculos.repository.PaymentRepository;
import com.nozama.aluguel_veiculos.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentRepository repository;

    @Mock
    private PaymentService service;

    @InjectMocks
    private PaymentController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testDeletePayment() throws Exception {
        doNothing().when(service).delete(1L);

        var result = mockMvc.perform(delete("/payment/1"))
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println("Status: " + result.getResponse().getStatus());
    }
}
