package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class IntegrationStep2_PizzaService_RepoTest {

    private PaymentRepository mockRepo;
    private PizzaService pizzaService;

    @BeforeEach
    void init() {
        mockRepo = mock(PaymentRepository.class);
        pizzaService = new PizzaService(null, mockRepo);
    }

    @Test
    void addPaymentShouldDelegateToRepo() {
        PaymentType type = PaymentType.Card;

        pizzaService.addPayment(2, type, 50.0);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(mockRepo, times(1)).add(captor.capture());

        Payment payment = captor.getValue();
        assertEquals(2, payment.getTableNumber());
        assertEquals(type, payment.getType());
        assertEquals(50.0, payment.getAmount());
    }

    @Test
    void getPaymentsShouldReturnFromRepo() {
        List<Payment> fakeList = List.of(new Payment(3, PaymentType.Cash, 30.0));
        when(mockRepo.getAll()).thenReturn(fakeList);

        List<Payment> result = pizzaService.getPayments();
        assertEquals(1, result.size());
        assertEquals(PaymentType.Cash, result.get(0).getType());
    }
}
