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
    void testFromEntity() {
        Customer customer = new Customer();
        customer.setNome("Murillão");

        Model model = new Model();
        model.setNome("ModeloX");

        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca("ABC1234");
        vehicle.setModel(model);

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setStartDate(LocalDate.now().minusDays(5));
        rental.setEndDate(LocalDate.now().plusDays(5));
        rental.setReturnDate(null);
        rental.setPrice(new BigDecimal("1000.00"));
        rental.setReturned(false);

        Fine fine = new Fine();
        fine.setId(10L);
        fine.setRental(rental);
        fine.setDescricao("Multa teste");
        fine.setValor(new BigDecimal("200.00"));
        fine.setData_multa(LocalDate.now());

        Inspection inspection = new Inspection();
        inspection.setId(20L);
        inspection.setRental(rental);
        inspection.setDescricao("Inspeção teste");
        inspection.setData_inspecao(LocalDate.now());
        inspection.setDanificado(false);

        Payment payment = new Payment();
        payment.setId(30L);
        payment.setRental(rental);
        payment.setData_pagamento(LocalDate.now());
        payment.setValor(new BigDecimal("500.00"));
        payment.setFormaPagto("Cartão");
        payment.setStatus("Pago");
        payment.setParcelas(2);

        rental.setFines(List.of(fine));
        rental.setInspections(List.of(inspection));
        rental.setPayments(List.of(payment));

        RentalResponse response = RentalResponse.fromEntity(rental);

        System.out.println("ID Rental: " + response.id());
        System.out.println("Placa: " + response.placa());
        System.out.println("Modelo: " + response.modelo());
        System.out.println("Customer Name: " + response.customerName());
        System.out.println("Start Date: " + response.startDate());
        System.out.println("End Date: " + response.endDate());
        System.out.println("Returned Date: " + response.returnedDate());
        System.out.println("Price: " + response.price());
        System.out.println("Returned: " + response.returned());
        System.out.println("Status: " + response.status());
        System.out.println("Fines size: " + response.fines().size());
        System.out.println("Inspections size: " + response.inspections().size());
        System.out.println("Payments size: " + response.payments().size());

        // Validar valores
        assertEquals(1L, response.id());
        assertEquals("ABC1234", response.placa());
        assertEquals("ModeloX", response.modelo());
        assertEquals("Murillão", response.customerName());
        assertEquals(new BigDecimal("1000.00"), response.price());
        assertFalse(response.returned());
        assertEquals("ATIVA", response.status());
        assertEquals(1, response.fines().size());
        assertEquals(1, response.inspections().size());
        assertEquals(1, response.payments().size());
    }

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
