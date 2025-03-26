package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import static org.junit.jupiter.api.Assertions.*;
class PizzaServiceTest {

    private MenuRepository repoMenu;
    private PaymentRepository payRepo;
    private PizzaService service;
    private int size;

    @BeforeEach
    void setUp() {
        this.repoMenu=new MenuRepository();
        this.payRepo= new PaymentRepository();
        this.service = new PizzaService(repoMenu, payRepo);
        this.size = service.getPayments().size();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addPayment() {
        service.addPayment(1, PaymentType.Card, 100);
        assertEquals(size + 1, service.getPayments().size());
        assertEquals(1, service.getPayments().get(size).getTableNumber());
        assertEquals(PaymentType.Card, service.getPayments().get(size).getType());
        assertEquals(100, service.getPayments().get(size).getAmount());
    }
}