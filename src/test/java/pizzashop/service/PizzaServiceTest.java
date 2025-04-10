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
        assertTrue(thrown.getMessage().contains("Table number and amount must be greater than 0"));
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
        assertTrue(thrown.getMessage().contains("Table number and amount must be greater than 0"));
    }

    //Teste BVA ~ Boundary Value Analysis
    @ParameterizedTest(name="BVA Valid Test {index}: table={0}, amount={1}")
    @CsvSource({
            "7, 0.01", // valid: limita inf pt table si amount
            "2, 50" // valid: val arbitrare, tot valide
    })
    @DisplayName("BVA Valid: Test parametrizat pt addPayment")
    @Tag("BVA")
    @Timeout(value=1, unit = TimeUnit.SECONDS)
    void testAddPayment_BVA_Valid(int table, double amount)
    {
        service.addPayment(table, PaymentType.Card, amount);
        List<Payment> payments = service.getPayments();
        assertEquals(initialSize + 1, payments.size());
        Payment p = payments.get(initialSize);
        assertEquals(table, p.getTableNumber());
        assertEquals(PaymentType.Card, p.getType());
        assertEquals(amount, p.getAmount());
    }

    @ParameterizedTest(name="BVA Invalid Test {index}: table={0}, amount={1}")
    @CsvSource({
            "0, 0.01", // invalid: table < 1
            "1, 0", // invalid: amount < 0
            "-1, -0.01" // invalid: table < 1 si amount < 0
    })
    @DisplayName("BVA Invalid: Test parametrizat pt addPayment")
    @Tag("BVA")
    @Timeout(value=1, unit = TimeUnit.SECONDS)
    void testAddPayment_BVA_Invalid(int table, double amount)
    {
        // se asteapta aruncarea exceptiei pt val non-valide
        IllegalArgumentException exceptie = assertThrows(
                IllegalArgumentException.class,
                () -> service.addPayment(table, PaymentType.Card, amount),
                "Expected addPayment() to throw, but it didn't"
        );
        assertTrue(exceptie.getMessage().contains("Table number and amount must be greater than 0"));
    }


}