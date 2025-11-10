package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.RentalRequest;
import com.nozama.aluguel_veiculos.repository.CustomerRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.repository.VehicleRepository;
import com.nozama.aluguel_veiculos.services.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RentalService rentalService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRentalVehicleNotAvailable() {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca("ABC1234");
        vehicle.setValorDiario(new BigDecimal("100.00"));
        vehicle.setStatus(VehicleStatus.ALUGADO);

        when(vehicleRepository.findById("ABC1234")).thenReturn(Optional.of(vehicle));

        RentalRequest request = new RentalRequest("12345678900", "ABC1234", LocalDate.now(), LocalDate.now().plusDays(2));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> rentalService.create(request));

        System.out.println("⚠️  Esperado erro: Veículo não disponível para aluguel | Recebido: " + ex.getReason());
    }

    @Test
    void testReturnVehicleSuccessfully() {
        Vehicle vehicle = new Vehicle();
        vehicle.setStatus(VehicleStatus.ALUGADO);

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setVehicle(vehicle);
        rental.setReturned(false);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(inv -> inv.getArguments()[0]);

        Rental result = rentalService.returnVehicle(1L);

        assertTrue(result.isReturned());
        assertEquals(VehicleStatus.DISPONIVEL, result.getVehicle().getStatus());

        System.out.println("🔁 Esperado: Veículo devolvido (true) | Obtido: " + result.isReturned());
        System.out.println("🚗 Esperado status: DISPONIVEL | Obtido: " + result.getVehicle().getStatus());
    }

    @Test
    void testFindRentalByIdNotFound() {
        when(rentalRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> rentalService.findRentalById(99L));

        System.out.println("❌ Esperado erro: Locação não encontrada | Recebido: " + ex.getReason());
    }
}
