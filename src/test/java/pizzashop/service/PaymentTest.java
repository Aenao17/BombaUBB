package pizzashop.service;

import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    void testConstructorAndGetters() {
        Payment payment = new Payment(3, PaymentType.Cash, 99.99);
        assertEquals(3, payment.getTableNumber());
        assertEquals(PaymentType.Cash, payment.getType());
        assertEquals(99.99, payment.getAmount());
    }

    @Test
    void testSetters() {
        Payment payment = new Payment(0, PaymentType.Cash, 0.0);
        payment.setTableNumber(5);
        payment.setType(PaymentType.Card);
        payment.setAmount(45.5);

        assertEquals(5, payment.getTableNumber());
        assertEquals(PaymentType.Card, payment.getType());
        assertEquals(45.5, payment.getAmount());
    }
}