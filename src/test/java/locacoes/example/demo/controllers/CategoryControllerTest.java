package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.CategoryController;
import com.nozama.aluguel_veiculos.domain.Category;
import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import com.nozama.aluguel_veiculos.repository.CategoryRepository;
import com.nozama.aluguel_veiculos.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    private CategoryService categoryService;
    private CategoryRepository categoryRepository;
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryService = Mockito.mock(CategoryService.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryService, categoryRepository);
    }

    @Test
    void testCreateCategory() {
        CategoryRequest request = new CategoryRequest("SUV", "Veículo esportivo");
        Category expected = new Category(1, "SUV", "Veículo esportivo");

        when(categoryService.create(any(CategoryRequest.class))).thenReturn(expected);

        ResponseEntity<Category> response = categoryController.create(request);
        Category actual = response.getBody();

        System.out.println("=== Test Create Category ===");
        System.out.println("Expected ID: " + expected.getId() + " | Actual ID: " + actual.getId());
        System.out.println("Expected Nome: " + expected.getNome() + " | Actual Nome: " + actual.getNome());
        System.out.println("Expected Descricao: " + expected.getDescricao() + " | Actual Descricao: " + actual.getDescricao());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getNome(), actual.getNome());
        assertEquals(expected.getDescricao(), actual.getDescricao());
    }

    @Test
    void testGetAllCategories() {
        Category expected = new Category(1, "SUV", "Veículo esportivo");

        when(categoryService.findAll()).thenReturn(List.of(expected));

        ResponseEntity<List<Category>> response = categoryController.getAll();
        List<Category> actualList = response.getBody();

        System.out.println("=== Test Get All Categories ===");
        System.out.println("Expected list size: 1 | Actual list size: " + actualList.size());
        System.out.println("Expected Nome: " + expected.getNome() + " | Actual Nome: " + actualList.get(0).getNome());
        System.out.println("Expected Descricao: " + expected.getDescricao() + " | Actual Descricao: " + actualList.get(0).getDescricao());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, actualList.size());
        assertEquals(expected.getNome(), actualList.get(0).getNome());
        assertEquals(expected.getDescricao(), actualList.get(0).getDescricao());
    }

    @Test
    void testGetByIdFound() {
        Category expected = new Category(1, "SUV", "Veículo esportivo");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(expected));

        ResponseEntity<Category> response = categoryController.getByName(1);
        Category actual = response.getBody();

        System.out.println("=== Test Get By ID Found ===");
        System.out.println("Expected ID: " + expected.getId() + " | Actual ID: " + actual.getId());
        System.out.println("Expected Nome: " + expected.getNome() + " | Actual Nome: " + actual.getNome());
        System.out.println("Expected Descricao: " + expected.getDescricao() + " | Actual Descricao: " + actual.getDescricao());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getNome(), actual.getNome());
        assertEquals(expected.getDescricao(), actual.getDescricao());
    }

    @Test
    void testGetByIdNotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Category> response = categoryController.getByName(1);

        System.out.println("=== Test Get By ID Not Found ===");
        System.out.println("Expected Status: 404 | Actual Status: " + response.getStatusCodeValue());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateCategory() {
        CategoryRequest request = new CategoryRequest("Sedan", "Veículo confortável");
        Category expected = new Category(1, "Sedan", "Veículo confortável");

        when(categoryService.update(eq(1), any(CategoryRequest.class))).thenReturn(expected);

        ResponseEntity<Category> response = categoryController.update(1, request);
        Category actual = response.getBody();

        System.out.println("=== Test Update Category ===");
        System.out.println("Expected Nome: " + expected.getNome() + " | Actual Nome: " + actual.getNome());
        System.out.println("Expected Descricao: " + expected.getDescricao() + " | Actual Descricao: " + actual.getDescricao());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected.getNome(), actual.getNome());
        assertEquals(expected.getDescricao(), actual.getDescricao());
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryService).deleteById(1);

        ResponseEntity<Void> response = categoryController.delete(1);

        System.out.println("=== Test Delete Category ===");
        System.out.println("Expected Status: 200 | Actual Status: " + response.getStatusCodeValue());

        assertEquals(200, response.getStatusCodeValue());
        verify(categoryService, times(1)).deleteById(1);
    }
}
