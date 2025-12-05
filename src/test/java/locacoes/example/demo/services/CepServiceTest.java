package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.services.CepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CepServiceTest {

    private CepService cepService;

    @BeforeEach
    void setup() {
        cepService = new CepService();

        try {
            var field = CepService.class.getDeclaredField("viaCepUrl");
            field.setAccessible(true);
            field.set(cepService, "https://viacep.com.br/ws/{cep}/json/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBuscarEnderecoValido() {
        String cep = "01001000";

        Map<String, Object> result = cepService.buscarEndereco(cep);

        System.out.println("BuscarEndereco returned: " + result);

        assertFalse(result.containsKey("erro"));
        assertEquals("01001-000", result.get("cep"));
        assertEquals("Praça da Sé", result.get("logradouro"));
    }

    @Test
    void testBuscarEnderecoInvalido() {
        String cepInvalido = "123";

        Map<String, Object> result = cepService.buscarEndereco(cepInvalido);

        System.out.println("BuscarEndereco inválido returned: " + result);

        assertTrue((Boolean) result.get("erro"));
        assertEquals("CEP inválido", result.get("mensagem"));
    }

}
