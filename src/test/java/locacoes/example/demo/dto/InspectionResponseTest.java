package locacoes.example.demo.dto;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.Inspection;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.dto.InspectionResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InspectionResponseTest {

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

        Inspection inspection = new Inspection();
        inspection.setId(10L);
        inspection.setRental(rental);
        inspection.setData_inspecao(LocalDate.now());
        inspection.setDescricao("Inspeção normal");
        inspection.setDanificado(false);

        InspectionResponse response = InspectionResponse.fromEntity(inspection);

        System.out.println("ID Inspection: " + response.id());
        System.out.println("ID Rental: " + response.rental().id());
        System.out.println("Vehicle Placa: " + response.rental().vehiclePlaca());
        System.out.println("Customer Nome: " + response.rental().customerNome());
        System.out.println("Data Inspeção: " + response.data_inspecao());
        System.out.println("Descrição: " + response.descricao());
        System.out.println("Danificado: " + response.danificado());

        assertEquals(10L, response.id());
        assertNotNull(response.rental());
        assertEquals(1L, response.rental().id());
        assertEquals("ABC1234", response.rental().vehiclePlaca());
        assertEquals("Murillão", response.rental().customerNome());
        assertEquals("Inspeção normal", response.descricao());
        assertFalse(response.danificado());
    }

    @Test
    void testFromEntitySummary() {
        Customer customer = new Customer();
        customer.setNome("ClienteResumo");

        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca("XYZ9876");

        Rental rental = new Rental();
        rental.setId(2L);
        rental.setCustomer(customer);
        rental.setVehicle(vehicle);

        Inspection inspection = new Inspection();
        inspection.setId(20L);
        inspection.setRental(rental);
        inspection.setData_inspecao(LocalDate.now());
        inspection.setDescricao("Inspeção resumo");
        inspection.setDanificado(true);

        InspectionResponse response = InspectionResponse.fromEntitySummary(inspection);

        System.out.println("ID Inspection: " + response.id());
        System.out.println("Rental Info: " + response.rental());
        System.out.println("Data Inspeção: " + response.data_inspecao());
        System.out.println("Descrição: " + response.descricao());
        System.out.println("Danificado: " + response.danificado());

        assertEquals(20L, response.id());
        assertNull(response.rental());
        assertEquals("Inspeção resumo", response.descricao());
        assertTrue(response.danificado());
    }
}
