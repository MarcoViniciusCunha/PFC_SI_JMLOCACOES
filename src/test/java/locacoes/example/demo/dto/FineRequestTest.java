package locacoes.example.demo.dto;

import com.nozama.aluguel_veiculos.dto.FineRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

class FineRequestTest {

    @Test
    void testUpdateRecord() {
        FineRequest.update update = new FineRequest.update(
                "ABC1234",
                "Descrição atualizada",
                new BigDecimal("150.00"),
                LocalDate.now()
        );

        System.out.println("Placa: " + update.placa());
        System.out.println("Descrição: " + update.descricao());
        System.out.println("Valor: " + update.valor());
        System.out.println("Data da multa: " + update.dataMulta());

        assert update.placa().equals("ABC1234");
        assert update.descricao().equals("Descrição atualizada");
        assert update.valor().equals(new BigDecimal("150.00"));
        assert update.dataMulta().equals(LocalDate.now());
    }
}
