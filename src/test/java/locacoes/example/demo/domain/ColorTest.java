package locacoes.example.demo.domain;

import com.nozama.aluguel_veiculos.domain.Color;
import com.nozama.aluguel_veiculos.dto.ColorRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorTest {

    @Test
    void testColorConstructorAndGetters() {
        ColorRequest request = new ColorRequest("Vermelho");
        Color color = new Color(request);

        color.setId(1);

        assertEquals(1, color.getId());
        assertEquals("Vermelho", color.getNome());

        System.out.println("Color ID: " + color.getId());
        System.out.println("Color Nome: " + color.getNome());
    }

    @Test
    void testColorSetters() {
        Color color = new Color();
        color.setId(2);
        color.setNome("Azul");

        assertEquals(2, color.getId());
        assertEquals("Azul", color.getNome());

        System.out.println("Color atualizado ID: " + color.getId());
        System.out.println("Color atualizado Nome: " + color.getNome());
    }
}
