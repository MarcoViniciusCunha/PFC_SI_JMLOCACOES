package locacoes.example.demo.dto;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.dto.FineResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FineResponseTest {

    @Test
    void testFromEntity() {
        Customer customer = new Customer();
        customer.setNome("Murillão");

        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca("ABC1234");

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setCustomer(customer);
        rental.setVehicle(vehicle);

        Fine fine = new Fine();
        fine.setId(10L);
        fine.setRental(rental);
        fine.setDescricao("Multa teste");
        fine.setValor(new BigDecimal("200.00"));
        fine.setData_multa(LocalDate.now());

        FineResponse response = FineResponse.fromEntity(fine);

        System.out.println("ID Fine: " + response.id());
        System.out.println("Rental ID: " + response.rental().id());
        System.out.println("Vehicle Placa: " + response.rental().vehiclePlaca());
        System.out.println("Customer Nome: " + response.rental().customerNome());
        System.out.println("Descrição: " + response.descricao());
        System.out.println("Valor: " + response.valor());
        System.out.println("Data da Multa: " + response.data_multa());

        assertEquals(10L, response.id());
        assertNotNull(response.rental());
        assertEquals(1L, response.rental().id());
        assertEquals("ABC1234", response.rental().vehiclePlaca());
        assertEquals("Murillão", response.rental().customerNome());
        assertEquals("Multa teste", response.descricao());
        assertEquals(new BigDecimal("200.00"), response.valor());
        assertEquals(fine.getData_multa(), response.data_multa());
    }
}
