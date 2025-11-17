package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.services.CepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CepServiceTest {

    private CepService cepService;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setUp() {
        cepService = new CepService("https://viacep.com.br/ws/{cep}/json/");
        restTemplateMock = mock(RestTemplate.class);
    }

    @Test
    void testBuscarEndereco() {
        Map<String, String> enderecoMock = new HashMap<>();
        enderecoMock.put("logradouro", "Rua Teste");
        enderecoMock.put("bairro", "Bairro Teste");
        enderecoMock.put("localidade", "Cidade Teste");
        enderecoMock.put("uf", "ST");

        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForObject("https://viacep.com.br/ws/{cep}/json/", Map.class, "12345678"))
                .thenReturn(enderecoMock);

        Map<String, String> endereco = restTemplate.getForObject(
                "https://viacep.com.br/ws/{cep}/json/",
                Map.class,
                "12345678"
        );

        System.out.println("Logradouro: " + endereco.get("logradouro"));
        System.out.println("Bairro: " + endereco.get("bairro"));
        System.out.println("Cidade: " + endereco.get("localidade"));
        System.out.println("UF: " + endereco.get("uf"));

        assertNotNull(endereco);
        assertEquals("Rua Teste", endereco.get("logradouro"));
        assertEquals("Bairro Teste", endereco.get("bairro"));
        assertEquals("Cidade Teste", endereco.get("localidade"));
        assertEquals("ST", endereco.get("uf"));
    }
}
