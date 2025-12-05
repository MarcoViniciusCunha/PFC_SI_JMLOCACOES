package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.Inspection;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.InspectionRequest;
import com.nozama.aluguel_veiculos.repository.InspectionRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.services.InspectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InspectionServiceTest {

    @Mock
    private InspectionRepository inspectionRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private InspectionService inspectionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInspection() {
        Rental rental = new Rental();
        rental.setId(1L);

        InspectionRequest request = new InspectionRequest(1L, LocalDate.now(), "Sem danos", false);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(inspectionRepository.save(any(Inspection.class))).thenAnswer(i -> i.getArguments()[0]);

        Inspection result = inspectionService.create(request);

        assertNotNull(result);
        System.out.println("✅ Teste de criação passou! Inspeção criada: " + result);
    }

    @Test
    void testFindByIdNotFound() {
        when(inspectionRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> inspectionService.findById(99L));

        System.out.println("⚠️  Teste de busca falhou como esperado: " + exception.getMessage());
    }

    @Test
    void testDeleteInspection() {
        Inspection inspection = new Inspection();
        inspection.setId(1L);

        when(inspectionRepository.findById(1L)).thenReturn(Optional.of(inspection));

        inspectionService.delete(1L);

        verify(inspectionRepository, times(1)).delete(inspection);
        System.out.println("🗑️  Teste de exclusão passou! Inspeção deletada com sucesso.");
    }

    @Test
    void testUpdateInspection() {
        Rental rental = new Rental();
        rental.setId(1L);

        Inspection inspection = new Inspection();
        inspection.setId(1L);
        inspection.setRental(rental);
        inspection.setDescricao("Velha");
        inspection.setDanificado(false);

        InspectionRequest.update request = new InspectionRequest.update(
                null, LocalDate.now(), "Nova descrição", true
        );

        when(inspectionRepository.save(any(Inspection.class))).thenAnswer(i -> i.getArguments()[0]);

        Inspection updated = inspectionService.update(inspection, request);

        assertEquals("Nova descrição", updated.getDescricao());
        assertTrue(updated.isDanificado());
        System.out.println("🔁 Teste de atualização passou! Nova descrição: " + updated.getDescricao());
    }
}
