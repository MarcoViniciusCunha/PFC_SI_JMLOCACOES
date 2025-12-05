package locacoes.example.demo.dto;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.Payment;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.PaymentStatus;
import com.nozama.aluguel_veiculos.dto.PaymentResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentResponseTest {

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

        Payment payment = new Payment();
        payment.setId(100L);
        payment.setRental(rental);
        payment.setData_pagamento(LocalDate.now());
        payment.setValor(new BigDecimal("500.00"));
        payment.setFormaPagto("Cartão");
        payment.setStatus(PaymentStatus.PAGO);
        payment.setParcelas(2);

        PaymentResponse response = PaymentResponse.fromEntity(payment);

        assertEquals(100L, response.id());
        assertNotNull(response.rental());
        assertEquals(1L, response.rental().id());
        assertEquals("ABC1234", response.rental().vehiclePlaca());
        assertEquals("Murillão", response.rental().customerNome());
        assertEquals(new BigDecimal("500.00"), response.valor());
        assertEquals("Cartão", response.formaPagto());

        assertEquals(PaymentStatus.PAGO.name(), response.status());

        assertEquals(2, response.parcelas());
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

        Payment payment = new Payment();
        payment.setId(200L);
        payment.setRental(rental);
        payment.setData_pagamento(LocalDate.now());
        payment.setValor(new BigDecimal("300.00"));
        payment.setFormaPagto("Dinheiro");
        payment.setStatus(PaymentStatus.PENDENTE);
        payment.setParcelas(1);

        PaymentResponse response = PaymentResponse.fromEntitySummary(payment);

        assertEquals(200L, response.id());
        assertNull(response.rental());
        assertEquals(new BigDecimal("300.00"), response.valor());
        assertEquals("Dinheiro", response.formaPagto());

        assertEquals(PaymentStatus.PENDENTE.name(), response.status());

        assertEquals(1, response.parcelas());
    }
}
