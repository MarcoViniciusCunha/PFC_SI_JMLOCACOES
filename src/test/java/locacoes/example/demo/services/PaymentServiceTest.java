package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.Payment;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.PaymentRequest;
import com.nozama.aluguel_veiculos.repository.PaymentRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment() {
        // Arrange
        Rental rental = new Rental();
        rental.setId(1L);

        PaymentRequest request = new PaymentRequest(
                1L,
                LocalDate.of(2025, 11, 10),
                new BigDecimal("250.00"),
                "CARTÃO",
                "PAGO",
                1,
                "Pagamento do aluguel"
        );

        Payment expectedPayment = new Payment(rental, request);
        expectedPayment.setId(10L);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        // Act
        Payment result = paymentService.create(request);

        // Assert
        System.out.println("✅ Esperado: " + expectedPayment.getValor() + " | Obtido: " + result.getValor());
        assertEquals(expectedPayment.getValor(), result.getValor());

        System.out.println("✅ Esperado: " + expectedPayment.getFormaPagto() + " | Obtido: " + result.getFormaPagto());
        assertEquals(expectedPayment.getFormaPagto(), result.getFormaPagto());

        System.out.println("✅ Esperado: " + expectedPayment.getStatus() + " | Obtido: " + result.getStatus());
        assertEquals(expectedPayment.getStatus(), result.getStatus());

        System.out.println("✅ Esperado: " + expectedPayment.getDescricao() + " | Obtido: " + result.getDescricao());
        assertEquals(expectedPayment.getDescricao(), result.getDescricao());

        verify(rentalRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testFindById() {
        Payment payment = new Payment();
        payment.setId(5L);
        payment.setValor(new BigDecimal("500.00"));

        when(paymentRepository.findById(5L)).thenReturn(Optional.of(payment));

        Payment result = paymentService.findById(5L);

        System.out.println("✅ Esperado: 500.00 | Obtido: " + result.getValor());
        assertEquals(new BigDecimal("500.00"), result.getValor());
    }
}
