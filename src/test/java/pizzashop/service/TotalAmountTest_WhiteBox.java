package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TotalAmountTest_WhiteBox {

    @AfterEach
    void tearDown() {
    }

    // Test 1: Cazul în care lista este null.
    @Test
    public void testGetTotalAmount_ListNull() {
        TestPizzaService service = new TestPizzaService();
        service.setTestPayments(null);
        double total = service.getTotalAmount(PaymentType.Card);
        assertEquals(0.0, total, "Dacă lista de plăți este null, totalul trebuie să fie 0.0");
    }

    // Test 2: Cazul în care lista este goală.
    @Test
    public void testGetTotalAmount_ListEmpty() {
        TestPizzaService service = new TestPizzaService();
        service.setTestPayments(Collections.emptyList());
        double total = service.getTotalAmount(PaymentType.Card);
        assertEquals(0.0, total, "Dacă lista de plăți este goală, totalul trebuie să fie 0.0");
    }

    // Test 3: Un singur Payment care nu corespunde tipului căutat.
    @Test
    public void testGetTotalAmount_SinglePaymentNotMatching() {
        TestPizzaService service = new TestPizzaService();

        Payment payment = new Payment(1, PaymentType.Cash, 100.0);
        service.setTestPayments(Collections.singletonList(payment));
        double total = service.getTotalAmount(PaymentType.Card);
        assertEquals(0.0, total, "Dacă tipul plății nu se potrivește, totalul trebuie să fie 0.0");
    }

    // Test 4: Un singur Payment care corespunde tipului căutat.
    @Test
    public void testGetTotalAmount_SinglePaymentMatching() {
        TestPizzaService service = new TestPizzaService();
        // Creăm un Payment de tip CARD.
        Payment payment = new Payment(1, PaymentType.Card, 150.0);
        service.setTestPayments(Collections.singletonList(payment));
        double total = service.getTotalAmount(PaymentType.Card);
        assertEquals(150.0, total, "Dacă plata corespunde, totalul trebuie să fie suma aferentă");
    }

    // Test 5: Mai multe Payments, mix de tipuri.
    @Test
    public void testGetTotalAmount_MultiplePayments() {
        TestPizzaService service = new TestPizzaService();
        Payment p1 = new Payment(1, PaymentType.Card, 150.0); // Se adaugă.
        Payment p2 = new Payment(2, PaymentType.Cash, 100.0); // Nu se adaugă.
        Payment p3 = new Payment(3, PaymentType.Card, 50.0);  // Se adaugă.
        Payment p4 = new Payment(4, PaymentType.Cash, 200.0); // Nu se adaugă.
        service.setTestPayments(Arrays.asList(p1, p2, p3, p4));
        double total = service.getTotalAmount(PaymentType.Card);

        assertEquals(200.0, total, "Totalul pentru tipul CARD trebuie să fie 200.0");
    }

    private static class TestPizzaService extends PizzaService {
        private List<Payment> testPayments;
        public TestPizzaService() {
            super(null, null);
        }

        public void setTestPayments(List<Payment> payments) {
            this.testPayments = payments;
        }

        @Override
        public List<Payment> getPayments() {
            return testPayments;
        }
    }
}