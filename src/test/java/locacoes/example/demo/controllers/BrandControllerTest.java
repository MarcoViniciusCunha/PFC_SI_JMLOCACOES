package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.BrandController;
import com.nozama.aluguel_veiculos.domain.Brand;
import com.nozama.aluguel_veiculos.domain.Model;
import com.nozama.aluguel_veiculos.dto.BrandRequest;
import com.nozama.aluguel_veiculos.repository.BrandRepository;
import com.nozama.aluguel_veiculos.repository.ModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandControllerTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ModelRepository modelRepository;

    @InjectMocks
    private BrandController brandController;

    private Brand brand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        brand = new Brand();
        brand.setId(1);
        brand.setNome("Toyota");
    }

    @Test
    void testCreateBrandSuccessfully() {
        BrandRequest request = new BrandRequest("Toyota");
        when(brandRepository.existsByNome("Toyota")).thenReturn(false);
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        ResponseEntity<?> response = brandController.create(request);

        System.out.println("Create Response: " + response.getBody());

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Brand);
        assertEquals("Toyota", ((Brand) response.getBody()).getNome());
    }

    @Test
    void testCreateBrandConflict() {
        BrandRequest request = new BrandRequest("Toyota");
        when(brandRepository.existsByNome("Toyota")).thenReturn(true);

        ResponseEntity<?> response = brandController.create(request);

        System.out.println("Conflict Response: " + response.getBody());

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Marca 'Toyota' já existe!", response.getBody());
    }

    @Test
    void testFindAllBrands() {
        when(brandRepository.findAll()).thenReturn(List.of(brand));

        ResponseEntity<List<Brand>> response = brandController.findAll();

        System.out.println("FindAll Response: " + response.getBody());

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testFindByIdFound() {
        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));

        ResponseEntity<Brand> response = brandController.findById(1);

        System.out.println("FindById Found Response: " + response.getBody());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Toyota", response.getBody().getNome());
    }

    @Test
    void testFindByIdNotFound() {
        when(brandRepository.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<Brand> response = brandController.findById(2);

        System.out.println("FindById NotFound Response: " + response.getStatusCodeValue());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateBrandFound() {
        Brand updatedBrand = new Brand();
        updatedBrand.setNome("Honda");

        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));
        when(brandRepository.save(any(Brand.class))).thenReturn(updatedBrand);

        ResponseEntity<Brand> response = brandController.update(1, updatedBrand);

        System.out.println("Update Response: " + response.getBody());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Honda", response.getBody().getNome());
    }

    @Test
    void testUpdateBrandNotFound() {
        Brand updatedBrand = new Brand();
        updatedBrand.setNome("Honda");

        when(brandRepository.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<Brand> response = brandController.update(2, updatedBrand);

        System.out.println("Update NotFound Response: " + response.getStatusCodeValue());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteBrandFound() {
        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));
        doNothing().when(brandRepository).deleteById(1);

        ResponseEntity<Void> response = brandController.delete(1);

        System.out.println("Delete Response: " + response.getStatusCodeValue());

        assertEquals(200, response.getStatusCodeValue());
        verify(brandRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBrandNotFound() {
        when(brandRepository.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = brandController.delete(2);

        System.out.println("Delete NotFound Response: " + response.getStatusCodeValue());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetModelsByBrand() {
        Model model = new Model();
        model.setId(1);
        model.setNome("Corolla");

        when(modelRepository.findByBrandId(1)).thenReturn(List.of(model));

        ResponseEntity<List<Model>> response = brandController.getModelsByBrand(1);

        System.out.println("Models Response: " + response.getBody());

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Corolla", response.getBody().get(0).getNome());
    }
}
