package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.dto.FineRequest;
import com.nozama.aluguel_veiculos.repository.FineRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.services.FineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private FineService fineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- CREATE ---
    @Test
    void deveCriarMultaComSucesso() {
        String placa = "ABC1234";
        LocalDate data = LocalDate.now();
        FineRequest request = mock(FineRequest.class);
        when(request.placa()).thenReturn(placa);
        when(request.dataMulta()).thenReturn(data);

        Rental rental = new Rental();
        when(rentalRepository.findRentalByVehiclePlateAndDate(placa, data))
                .thenReturn(Optional.of(rental));

        Fine expectedFine = new Fine(rental, request);
        when(fineRepository.save(any(Fine.class))).thenReturn(expectedFine);

        Fine actual = fineService.create(request);

        System.out.println("✅ Expected: Fine vinculada ao rental " + rental);
        System.out.println("✅ Actual:   " + actual);

        assertEquals(expectedFine, actual);
        verify(fineRepository).save(any(Fine.class));
        verify(rentalRepository).findRentalByVehiclePlateAndDate(placa, data);
    }

    @Test
    void deveLancarExcecaoAoCriarMultaSemRental() {
        String placa = "ZZZ9999";
        LocalDate data = LocalDate.now();
        FineRequest request = mock(FineRequest.class);
        when(request.placa()).thenReturn(placa);
        when(request.dataMulta()).thenReturn(data);

        when(rentalRepository.findRentalByVehiclePlateAndDate(placa, data))
                .thenReturn(Optional.empty());

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> fineService.create(request));

        System.out.println("❌ Expected: ResponseStatusException (locação não encontrada)");
        System.out.println("❌ Actual:   " + e.getMessage());
    }

    // --- FIND BY ID ---
    @Test
    void deveBuscarMultaPorId() {
        Fine expected = new Fine();
        when(fineRepository.findById(1L)).thenReturn(Optional.of(expected));

        Fine actual = fineService.findById(1L);

        System.out.println("✅ Expected: " + expected);
        System.out.println("✅ Actual:   " + actual);

        assertEquals(expected, actual);
        verify(fineRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoAoBuscarMultaInexistente() {
        when(fineRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> fineService.findById(1L));

        System.out.println("❌ Expected: ResponseStatusException (multa não encontrada)");
        System.out.println("❌ Actual:   " + e.getMessage());
    }

    // --- DELETE ---
    @Test
    void deveDeletarMultaComSucesso() {
        Fine fine = new Fine();
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));

        fineService.delete(1L);

        System.out.println("✅ Expected: delete() chamado com multa existente");
        verify(fineRepository).delete(fine);
    }

    // --- UPDATE ---
    @Test
    void deveAtualizarCamposDaMulta() {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca("ABC1234");

        Rental rental = new Rental();
        rental.setVehicle(vehicle);

        Fine fine = new Fine();
        fine.setData_multa(LocalDate.of(2024, 10, 10));
        fine.setDescricao("Multa antiga");
        fine.setValor(new BigDecimal("100.00"));
        fine.setRental(rental);

        FineRequest.update request = mock(FineRequest.update.class);
        when(request.dataMulta()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.descricao()).thenReturn("Nova descrição");
        when(request.valor()).thenReturn(new BigDecimal("200.00"));
        when(request.placa()).thenReturn("ABC1234");

        when(fineRepository.save(any(Fine.class))).thenReturn(fine);

        Fine updated = fineService.update(fine, request);

        System.out.println("✅ Expected: descrição='Nova descrição', valor=200.00");
        System.out.println("✅ Actual:   descrição='" + updated.getDescricao() + "', valor=" + updated.getValor());

        assertEquals("Nova descrição", updated.getDescricao());
        assertEquals(new BigDecimal("200.00"), updated.getValor());
        verify(fineRepository).save(fine);
    }

    @Test
    void deveLancarExcecaoQuandoAlterarPlacaInexistente() {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca("OLD1234");

        Rental rental = new Rental();
        rental.setVehicle(vehicle);

        Fine fine = new Fine();
        fine.setRental(rental);
        fine.setData_multa(LocalDate.now());

        FineRequest.update request = mock(FineRequest.update.class);
        when(request.placa()).thenReturn("NEW9999");
        when(request.dataMulta()).thenReturn(LocalDate.now());

        when(rentalRepository.findRentalByVehiclePlateAndDate(anyString(), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> fineService.update(fine, request));

        System.out.println("❌ Expected: ResponseStatusException (locação não encontrada)");
        System.out.println("❌ Actual:   " + e.getMessage());
    }
}
