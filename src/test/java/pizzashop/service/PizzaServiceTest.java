package pizzashop.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
class PizzaServiceTest {

    private MenuRepository repoMenu;
    private PaymentRepository payRepo;
    private PizzaService service;
    private int initialSize;

    @BeforeEach
    void setUp() {
        this.repoMenu=new MenuRepository();
        this.payRepo= new PaymentRepository();
        this.service = new PizzaService(repoMenu, payRepo);
        this.initialSize = service.getPayments().size();
    }

    @AfterEach
    void tearDown() {
    }

    //Teste ECP ~ Equivalence Class Partitioning
    @Test
    @DisplayName("ECP Valid: table=1, amount=100")
    @Tag("ECP")
    @Timeout(value=1, unit = TimeUnit.SECONDS)
    void testAddPayment_ECP_Valid()
    {
        service.addPayment(1, PaymentType.Card, 100);
        List<Payment> payments = service.getPayments();
        assertEquals(initialSize + 1, payments.size());

        Payment p = payments.get(initialSize);
        assertEquals(1, p.getTableNumber());
        assertEquals(PaymentType.Card, p.getType());
        assertEquals(100, p.getAmount());
    }
    @Test
    @DisplayName("ECP Invalid (table): table=0, amount=100")
    @Tag("ECP")
    @Timeout(value=1, unit = TimeUnit.SECONDS)
    void testAddPayment_ECP_InvalidTable()
    {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> service.addPayment(0,PaymentType.Card,100),
                "Expected addPayment to throw, but it didn't."
        );
        assertTrue(thrown.getMessage().contains("Table number and amount must be greater than 0."));
    }

    @Test
    @DisplayName("ECP Invalid (amount): table=1, amount=-50")
    @Tag("ECP")
    @Timeout(value=1, unit = TimeUnit.SECONDS)
    void testAddPayment_ECP_BothInvalid()
    {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> service.addPayment(-1, PaymentType.Card, -50),
                "Expected addPayment() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Table number and amount must be greater than 0."));
    }

    //Teste BVA ~ Boundary Value Analysis
    @Test
    @DisplayName("BVA Valid: Test Case 1 - Lower limits")
    @Tag("BVA")
    @Timeout(value=1, unit = TimeUnit.SECONDS)
    void testAddPayment_BVA_Valid() {
        int table = 7;
        double amount = 0.01;

        service.addPayment(table, PaymentType.Card, amount);
        List<Payment> payments = service.getPayments();
        assertEquals(initialSize + 1, payments.size());
        Payment p = payments.get(initialSize);
        assertEquals(table, p.getTableNumber());
        assertEquals(PaymentType.Card, p.getType());
        assertEquals(amount, p.getAmount());
    }

    @Test
    @DisplayName("BVA Invalid: Test Case 1 - Invalid table")
    @Tag("BVA")
    @Timeout(value=1, unit = TimeUnit.SECONDS)
    void testAddPayment_BVA_Invalid() {
        int table = 0;
        double amount = 0.01;

        // se asteapta aruncarea exceptiei pt val non-valide
        IllegalArgumentException exceptie = assertThrows(
                IllegalArgumentException.class,
                () -> service.addPayment(table, PaymentType.Card, amount),
                "Expected addPayment() to throw, but it didn't"
        );
        assertTrue(exceptie.getMessage().contains("Table number and amount must be greater than 0."));
    }


}