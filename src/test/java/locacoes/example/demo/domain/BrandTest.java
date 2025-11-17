package locacoes.example.demo.domain;

import com.nozama.aluguel_veiculos.domain.Brand;
import com.nozama.aluguel_veiculos.dto.BrandRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BrandTest {

    @Test
    void testBrandConstructorAndGetters() {
        BrandRequest request = new BrandRequest("Toyota");
        Brand brand = new Brand(request);

        brand.setId(1);
        brand.setModelos(new ArrayList<>());

        assertEquals(1, brand.getId());
        assertEquals("Toyota", brand.getNome());
        assertNotNull(brand.getModelos());

        System.out.println("Brand ID: " + brand.getId());
        System.out.println("Brand Nome: " + brand.getNome());
        System.out.println("Brand Modelos: " + brand.getModelos());
    }

    @Test
    void testBrandSetters() {
        Brand brand = new Brand();
        brand.setId(2);
        brand.setNome("Honda");

        assertEquals(2, brand.getId());
        assertEquals("Honda", brand.getNome());

        System.out.println("Brand atualizado ID: " + brand.getId());
        System.out.println("Brand atualizado Nome: " + brand.getNome());
    }
}
