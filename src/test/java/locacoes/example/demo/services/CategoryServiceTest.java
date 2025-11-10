package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.Category;
import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import com.nozama.aluguel_veiculos.repository.CategoryRepository;
import com.nozama.aluguel_veiculos.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_DeveSalvarCategoriaComSucesso() {
        CategoryRequest request = new CategoryRequest("SUV", "Carros grandes");
        Category saved = new Category(1, "SUV", "Carros grandes");

        when(repository.existsByNome("SUV")).thenReturn(false);
        when(repository.save(any(Category.class))).thenReturn(saved);

        Category result = service.create(request);

        assertThat(result).isEqualTo(saved);
        System.out.println("✅ create(): Categoria criada com sucesso → " + result.getNome());
        verify(repository).save(any(Category.class));
    }

    @Test
    void create_DeveLancarExcecaoSeNomeExistir() {
        CategoryRequest request = new CategoryRequest("SUV", "Duplicado");
        when(repository.existsByNome("SUV")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.create(request));

        assertThat(ex.getReason()).contains("já existe");
        System.out.println("✅ create(): Lançou exceção corretamente para nome duplicado.");
    }

    @Test
    void findAll_DeveRetornarTodasCategorias() {
        when(repository.findAll()).thenReturn(List.of(new Category(1, "SUV", "Desc")));
        List<Category> result = service.findAll();

        assertThat(result).hasSize(1);
        System.out.println("✅ findAll(): Retornou lista com " + result.size() + " categoria(s).");
    }

    @Test
    void findById_DeveRetornarCategoriaExistente() {
        Category category = new Category(1, "SUV", "Carros grandes");
        when(repository.findById(1)).thenReturn(Optional.of(category));

        Category result = service.findById(1);

        assertThat(result).isEqualTo(category);
        System.out.println("✅ findById(): Encontrou categoria → " + result.getNome());
    }

    @Test
    void findById_DeveLancarExcecaoSeNaoExistir() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.findById(99));

        assertThat(ex.getReason()).contains("não encontrada");
        System.out.println("✅ findById(): Lançou exceção corretamente para ID inexistente.");
    }

    @Test
    void patchById_DeveAtualizarNomeEDescricao() {
        Category category = new Category(1, "SUV", "Antiga");
        CategoryRequest request = new CategoryRequest("Sedan", "Atualizada");

        when(repository.findById(1)).thenReturn(Optional.of(category));
        when(repository.findByNome("Sedan")).thenReturn(Optional.empty());
        when(repository.save(any(Category.class))).thenReturn(category);

        Category result = service.patchById(1, request);

        assertThat(result.getNome()).isEqualTo("Sedan");
        assertThat(result.getDescricao()).isEqualTo("Atualizada");
        System.out.println("✅ patchById(): Atualizou nome e descrição com sucesso.");
    }

    @Test
    void patchById_DeveLancarExcecaoSeNomeDuplicado() {
        Category existing = new Category(1, "SUV", "Antiga");
        Category other = new Category(2, "Sedan", "Outro");

        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.findByNome("Sedan")).thenReturn(Optional.of(other));

        CategoryRequest request = new CategoryRequest("Sedan", "Nova descrição");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.patchById(1, request));

        assertThat(ex.getReason()).contains("já existe");
        System.out.println("✅ patchById(): Lançou exceção corretamente para nome duplicado.");
    }

    @Test
    void deleteById_DeveDeletarComSucesso() {
        when(repository.existsById(1)).thenReturn(true);

        service.deleteById(1);

        verify(repository).deleteById(1);
        System.out.println("✅ deleteById(): Categoria deletada com sucesso.");
    }

    @Test
    void deleteById_DeveLancarExcecaoSeNaoExistir() {
        when(repository.existsById(99)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.deleteById(99));

        assertThat(ex.getReason()).contains("não encontrada");
        System.out.println("✅ deleteById(): Lançou exceção corretamente para ID inexistente.");
    }
}
