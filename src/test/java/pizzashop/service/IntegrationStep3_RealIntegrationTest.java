package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationStep3_RealIntegrationTest {

    private PizzaService pizzaService;
    private List<Payment> inMemoryPayments;

    @BeforeEach
    void setUp() {
        inMemoryPayments = new ArrayList<>();

        PaymentRepository inMemoryRepo = new PaymentRepository() {
            @Override
            public void add(Payment payment) {
                inMemoryPayments.add(payment);
            }

            @Override
            public List<Payment> getAll() {
                return new ArrayList<>(inMemoryPayments);
            }

            @Override
            public void writeAll() {
                // no-op
            }
        };

        pizzaService = new PizzaService(null, inMemoryRepo);
    }

    @Test
    void addAndGetTotalAmountShouldWorkWithRealRepo() {
        pizzaService.addPayment(1, PaymentType.CARD, 50.0);
        double total = pizzaService.getTotalAmount(PaymentType.CARD);
        assertEquals(50.0, total);
    }

    @Test
    void getAllPaymentsShouldReturnAll() {
        pizzaService.addPayment(1, PaymentType.CARD, 20.0);
        pizzaService.addPayment(2, PaymentType.CASH, 30.0);
        assertEquals(2, inMemoryPayments.size());
    }
}