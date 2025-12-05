package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.PdfController;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.services.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class PdfControllerTest {

    private MockMvc mockMvc;
    private RentalRepository rentalRepository;
    private PdfService pdfService;

    @BeforeEach
    void setup() {
        rentalRepository = Mockito.mock(RentalRepository.class);
        pdfService = Mockito.mock(PdfService.class);

        PdfController pdfController = new PdfController(rentalRepository, pdfService);

        mockMvc = MockMvcBuilders.standaloneSetup(pdfController).build();
    }

    @Test
    void shouldReturnPdfWhenRentalExists() throws Exception {

        Rental rental = new Rental();
        rental.setId(1L);

        byte[] pdfBytes = "fake pdf".getBytes();

        Mockito.when(rentalRepository.findById(1L))
                .thenReturn(Optional.of(rental));

        Mockito.when(pdfService.gerarContrato(rental))
                .thenReturn(pdfBytes);

        mockMvc.perform(get("/contrato/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=contrato_1.pdf"))
                .andExpect(content().bytes(pdfBytes));
    }
}
