package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.*;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.dto.VehicleResponse;
import com.nozama.aluguel_veiculos.repository.*;
import com.nozama.aluguel_veiculos.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    private VehicleRepository vehicleRepository;
    private CategoryRepository categoryRepository;
    private InsuranceRepository insuranceRepository;
    private BrandRepository markRepository;
    private ColorRepository colorRepository;
    private ModelRepository modelRepository;

    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        vehicleRepository = mock(VehicleRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        insuranceRepository = mock(InsuranceRepository.class);
        markRepository = mock(BrandRepository.class);
        colorRepository = mock(ColorRepository.class);
        modelRepository = mock(ModelRepository.class);

        vehicleService = new VehicleService(vehicleRepository, categoryRepository, insuranceRepository,
                markRepository, colorRepository, modelRepository);
    }

    @Test
    void testCreateVehicleSuccess() {
        // Mock da request
        VehicleRequest request = mock(VehicleRequest.class);
        when(request.idCategoria()).thenReturn(1);
        when(request.idMarca()).thenReturn(2);
        when(request.idModelo()).thenReturn(3);
        when(request.idCor()).thenReturn(4);
        when(request.idSeguro()).thenReturn(5);
        when(request.placa()).thenReturn("ABC1234");
        when(request.descricao()).thenReturn("Carro novo");
        when(request.ano()).thenReturn(2023);
        when(request.valorDiario()).thenReturn(null);
        when(request.status()).thenReturn("DISPONIVEL"); // 🔑 importante pra não quebrar

        // Mocks dos repositórios
        Category category = new Category();
        category.setId(1);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Insurance insurance = new Insurance();
        insurance.setId(5);
        when(insuranceRepository.findById(5)).thenReturn(Optional.of(insurance));

        Brand brand = new Brand();
        brand.setId(2);
        when(markRepository.findById(2)).thenReturn(Optional.of(brand));

        Color color = new Color();
        color.setId(4);
        when(colorRepository.findById(4)).thenReturn(Optional.of(color));

        Model model = new Model();
        model.setId(3);
        when(modelRepository.findById(3)).thenReturn(Optional.of(model));

        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle vehicle = vehicleService.create(request);

        assertNotNull(vehicle);
        assertEquals("ABC1234", vehicle.getPlaca());
        assertEquals(VehicleStatus.DISPONIVEL, vehicle.getStatus());
        assertEquals(category, vehicle.getCategory());
        assertEquals(insurance, vehicle.getInsurance());
        assertEquals(brand, vehicle.getBrand());
        assertEquals(color, vehicle.getColor());
        assertEquals(model, vehicle.getModel());
    }

    @Test
    void testCreateVehicleCategoryNotFound() {
        VehicleRequest request = mock(VehicleRequest.class);
        when(request.idCategoria()).thenReturn(99);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> vehicleService.create(request));

        assertEquals("Categoria não encontrada.", ex.getReason());
    }

    @Test
    void testFindByIdSuccess() {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca("ABC1234");
        when(vehicleRepository.findById("ABC1234")).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.findById("ABC1234");
        assertEquals(vehicle, result);
    }

    @Test
    void testFindByIdNotFound() {
        when(vehicleRepository.findById("XYZ9999")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> vehicleService.findById("XYZ9999"));
        assertEquals("Veículo não encontrado.", ex.getReason());
    }

    @Test
    void testDeleteVehicleSuccess() {
        when(vehicleRepository.existsById("ABC1234")).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById("ABC1234");

        assertDoesNotThrow(() -> vehicleService.delete("ABC1234"));
        verify(vehicleRepository, times(1)).deleteById("ABC1234");
    }

    @Test
    void testDeleteVehicleNotFound() {
        when(vehicleRepository.existsById("XYZ9999")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> vehicleService.delete("XYZ9999"));
        assertEquals("Veículo não encontrado.", ex.getReason());
    }
}
