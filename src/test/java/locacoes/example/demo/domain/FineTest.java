package locacoes.example.demo.domain;

import com.nozama.aluguel_veiculos.domain.Fine;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FineTest {

    @Test
    void testFineSetters() {
        Fine fine = new Fine();
        fine.setId(2L);
        fine.setDescricao("Multa Extra");
        fine.setValor(new BigDecimal("300.00"));
        fine.setData_multa(LocalDate.of(2025, 11, 12));

        assertEquals(2L, fine.getId());
        assertEquals("Multa Extra", fine.getDescricao());
        assertEquals(new BigDecimal("300.00"), fine.getValor());

        System.out.println("Fine atualizado ID: " + fine.getId());
        System.out.println("Fine atualizado Descricao: " + fine.getDescricao());
        System.out.println("Fine atualizado Valor: " + fine.getValor());
        System.out.println("Fine atualizado Data Multa: " + fine.getData_multa());
    }
}
