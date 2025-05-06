package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentRepoTest {

    private PaymentRepository paymentRepo;

    // Subclasă pentru testare (evităm scrierea și citirea din fișier)
    public static class TestablePaymentRepo extends PaymentRepository {
        @Override
        public void writeAll() {
            // evită scrierea în fișier
        }

        @Override
        public void readPayments() {
            // evită citirea din fișier
        }
    }

    @BeforeEach
    void setUp() {
        paymentRepo = Mockito.spy(new TestablePaymentRepo());
    }

    @Test
    void testAddPayment() {
        Payment payment = new Payment(2, PaymentType.Cash, 30.0);
        paymentRepo.add(payment);

        List<Payment> payments = paymentRepo.getAll();
        assertEquals(1, payments.size());
        assertEquals(30.0, payments.get(0).getAmount());
        verify(paymentRepo).writeAll(); // confirmă apelul
    }

    @Test
    void testAddMultiplePayments() {
        paymentRepo.add(new Payment(1, PaymentType.Card, 25.5));
        paymentRepo.add(new Payment(2, PaymentType.Cash, 12.0));

        assertEquals(2, paymentRepo.getAll().size());
        verify(paymentRepo, times(2)).writeAll();
    }
}