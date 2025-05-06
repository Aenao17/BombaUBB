package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PizzaServiceT {

    private PaymentRepository mockPaymentRepo;
    private MenuRepository mockMenuRepo;
    private PizzaService pizzaService;

    @BeforeEach
    void setUp() {
        mockPaymentRepo = mock(PaymentRepository.class);
        mockMenuRepo = mock(MenuRepository.class); // nu folosim în aceste teste
        pizzaService = new PizzaService(mockMenuRepo, mockPaymentRepo);
    }

    @Test
    void testAddPaymentValid() {
        pizzaService.addPayment(1, PaymentType.Cash, 33.0);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(mockPaymentRepo).add(captor.capture());

        Payment added = captor.getValue();
        assertEquals(1, added.getTableNumber());
        assertEquals(PaymentType.Cash, added.getType());
        assertEquals(33.0, added.getAmount());
    }

    @Test
    void testAddPaymentInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                pizzaService.addPayment(0, PaymentType.Card, 50.0));

        assertThrows(IllegalArgumentException.class, () ->
                pizzaService.addPayment(1, PaymentType.Cash, -10.0));

        // Verificăm că nu a fost apelată metoda `add` în repo
        verify(mockPaymentRepo, never()).add(any());
    }

    @Test
    void testGetTotalAmountForType() {
        List<Payment> fakePayments = List.of(
                new Payment(1, PaymentType.Cash, 20.0),
                new Payment(2, PaymentType.Cash, 30.0),
                new Payment(3, PaymentType.Card, 50.0)
        );
        when(mockPaymentRepo.getAll()).thenReturn(fakePayments);

        double totalCash = pizzaService.getTotalAmount(PaymentType.Cash);
        double totalCard = pizzaService.getTotalAmount(PaymentType.Card);

        assertEquals(50.0, totalCash);
        assertEquals(50.0, totalCard);
    }
}
