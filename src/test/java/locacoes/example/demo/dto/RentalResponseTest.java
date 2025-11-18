package locacoes.example.demo.dto;

import com.nozama.aluguel_veiculos.domain.*;
import com.nozama.aluguel_veiculos.dto.RentalResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentalResponseTest {


    @Test
    void testFromEntityBasic() {
        Customer customer = new Customer();
        customer.setNome("ClienteBasic");

        Model model = new Model();
        model.setNome("ModeloB");

        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca("XYZ9876");
        vehicle.setModel(model);

        Rental rental = new Rental();
        rental.setId(2L);
        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setStartDate(LocalDate.now().minusDays(2));
        rental.setEndDate(LocalDate.now().plusDays(3));
        rental.setPrice(new BigDecimal("800.00"));
        rental.setReturned(false);

        RentalResponse response = RentalResponse.fromEntityBasic(rental);

        System.out.println("ID Rental: " + response.id());
        System.out.println("Placa: " + response.placa());
        System.out.println("Modelo: " + response.modelo());
        System.out.println("Customer Name: " + response.customerName());
        System.out.println("Status: " + response.status());
        System.out.println("Fines: " + response.fines());
        System.out.println("Inspections: " + response.inspections());
        System.out.println("Payments: " + response.payments());

        assertEquals(2L, response.id());
        assertEquals("XYZ9876", response.placa());
        assertEquals("ModeloB", response.modelo());
        assertEquals("ClienteBasic", response.customerName());
        assertEquals("ATIVA", response.status());
        assertNull(response.fines());
        assertNull(response.inspections());
        assertNull(response.payments());
    }
}
