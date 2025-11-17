package locacoes.example.demo.domain;

import com.nozama.aluguel_veiculos.domain.Category;
import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    @Test
    void testCategoryConstructorAndGetters() {
        CategoryRequest request = new CategoryRequest("Compacto", "Carro pequeno econômico");
        Category category = new Category(request);

        category.setId(1);

        assertEquals(1, category.getId());
        assertEquals("Compacto", category.getNome());
        assertEquals("Carro pequeno econômico", category.getDescricao());

        System.out.println("Category ID: " + category.getId());
        System.out.println("Category Nome: " + category.getNome());
        System.out.println("Category Descricao: " + category.getDescricao());
    }

    @Test
    void testCategorySetters() {
        Category category = new Category();
        category.setId(2);
        category.setNome("SUV");
        category.setDescricao("Carro grande e confortável");

        assertEquals(2, category.getId());
        assertEquals("SUV", category.getNome());
        assertEquals("Carro grande e confortável", category.getDescricao());

        System.out.println("Category atualizado ID: " + category.getId());
        System.out.println("Category atualizado Nome: " + category.getNome());
        System.out.println("Category atualizado Descricao: " + category.getDescricao());
    }
}
