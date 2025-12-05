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
import com.nozama.aluguel_veiculos.services.VehicleService;
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

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private RentalService rentalService;

    private Vehicle vehicle;
    private Customer customer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        vehicle = new Vehicle();
        vehicle.setPlaca("ABC1234");
        vehicle.setStatus(VehicleStatus.DISPONIVEL);
        vehicle.setValorDiario(BigDecimal.valueOf(100));

        customer = new Customer();
        customer.setId(1L);
        customer.setNome("Murillo");
    }

    @Test
    void testCreateRentalSuccess() {
        RentalRequest request = new RentalRequest("ABC1234", 1L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        when(vehicleRepository.findById("ABC1234")).thenReturn(Optional.of(vehicle));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.existsActiveConflict(vehicle, request.startDate(), request.endDate())).thenReturn(false);
        when(rentalRepository.save(any(Rental.class))).thenAnswer(i -> i.getArgument(0));

        Rental rental = rentalService.create(request);

        assertNotNull(rental);
        assertEquals(vehicle, rental.getVehicle());
        assertEquals(customer, rental.getCustomer());
        assertEquals(BigDecimal.valueOf(200).setScale(2), rental.getPrice());

        verify(rentalRepository, times(1)).save(any(Rental.class));
        verify(vehicleService, times(1)).atualizarStatusDoVeiculo(vehicle);
    }

    @Test
    void testCreateRentalVehicleAlreadyBooked() {
        RentalRequest request = new RentalRequest("ABC1234", 1L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        when(vehicleRepository.findById("ABC1234")).thenReturn(Optional.of(vehicle));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.existsActiveConflict(vehicle, request.startDate(), request.endDate())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> rentalService.create(request));
        assertEquals("400 BAD_REQUEST \"O veículo já possui uma locação nesse período.\"", exception.getMessage());
    }


    @Test
    void testReturnVehicleAlreadyReturned() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setReturned(true);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> rentalService.returnVehicle(1L));
        assertEquals("400 BAD_REQUEST \"Veículo já devolvido.\"", exception.getMessage());
    }
}
