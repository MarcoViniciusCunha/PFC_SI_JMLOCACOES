package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.domain.Brand;
import com.nozama.aluguel_veiculos.domain.Model;
import com.nozama.aluguel_veiculos.dto.ModelRequest;
import com.nozama.aluguel_veiculos.repository.BrandRepository;
import com.nozama.aluguel_veiculos.repository.ModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ModelControllerTest {

    @Mock
    private ModelRepository repository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private com.nozama.aluguel_veiculos.controllers.ModelController controller;

    private Brand brand;
    private Model model1;
    private Model model2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        brand = new Brand();
        brand.setId(1);
        brand.setNome("Honda");

        model1 = new Model();
        model1.setId(1);
        model1.setNome("Civic");
        model1.setBrand(brand);

        model2 = new Model();
        model2.setId(2);
        model2.setNome("Fit");
        model2.setBrand(brand);
    }

    @Test
    void testCreateModel_Success() {
        ModelRequest request = new ModelRequest("City", 1);

        when(repository.existsByNome("City")).thenReturn(false);
        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));
        when(repository.save(any(Model.class))).thenReturn(model1);

        ResponseEntity<?> response = controller.create(request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Model);
        assertEquals("Civic", ((Model) response.getBody()).getNome());

        System.out.println("Created Model: " + ((Model) response.getBody()).getNome() + " (ID: " + ((Model) response.getBody()).getId() + ")");
    }

    @Test
    void testCreateModel_AlreadyExists() {
        ModelRequest request = new ModelRequest("Civic", 1);

        when(repository.existsByNome("Civic")).thenReturn(true);

        ResponseEntity<?> response = controller.create(request);

        assertEquals(409, response.getStatusCodeValue());
        System.out.println(response.getBody());
    }

    @Test
    void testGetAllModels() {
        when(repository.findAll()).thenReturn(Arrays.asList(model1, model2));

        ResponseEntity<List<Model>> response = controller.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

        response.getBody().forEach(m -> System.out.println("Model ID: " + m.getId() + ", Name: " + m.getNome()));
    }

    @Test
    void testGetById_Found() {
        when(repository.findById(1)).thenReturn(Optional.of(model1));

        ResponseEntity<Model> response = controller.getById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Civic", response.getBody().getNome());

        System.out.println("GetById Model: " + response.getBody().getNome() + " (ID: " + response.getBody().getId() + ")");
    }

    @Test
    void testGetById_NotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<Model> response = controller.getById(99);

        assertEquals(404, response.getStatusCodeValue());
        System.out.println("Model not found for ID 99");
    }

    @Test
    void testUpdateModel_Success() {
        ModelRequest.update updateRequest = new ModelRequest.update("City", 1);
        when(repository.findById(1)).thenReturn(Optional.of(model1));
        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));
        when(repository.save(any(Model.class))).thenReturn(model1);

        ResponseEntity<?> response = controller.update(1, updateRequest);

        assertEquals(200, response.getStatusCodeValue());
        System.out.println("Updated Model: " + ((Model) response.getBody()).getNome());
    }
}