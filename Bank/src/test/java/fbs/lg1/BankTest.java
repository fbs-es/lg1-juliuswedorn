package fbs.lg1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankTest {

    private Account ac;

    @BeforeEach
    void setUp() {
        ac = new Account("Max MusterFrau", 10000000.00f, "At");
    }

    @Test
    @DisplayName("Should correctly set fields when initialize is called with valid parameters")
    void testinitialize() throws Exception {
        Method initializeMethod = Account.class.getDeclaredMethod("initialize", String.class, float.class, String.class);
        initializeMethod.setAccessible(true);

        initializeMethod.invoke(ac, "Jane Doe", 500.00f, "at");

        assertThat(getFieldValue("Name")).isEqualTo("Jane Doe");
        assertThat((float) getFieldValue("Balance")).isEqualTo(500.00f);

        long accountNumber = (long) getFieldValue("AccountNumber");
        assertThat(accountNumber).isBetween(10000000L, 99999999L);

        String iban = (String) getFieldValue("IBAN");
        assertThat(iban).isNotNull().startsWith("AT");
    }

    @Test
    @DisplayName("Should set Balance to 0 when negative balance is passed to initialize")
    void testInitializeNegativeBalance() throws Exception {
        Method initializeMethod = Account.class.getDeclaredMethod("initialize", String.class, float.class, String.class);
        initializeMethod.setAccessible(true);

        initializeMethod.invoke(ac, "John Doe", -150.00f, "de");

        assertThat((float) getFieldValue("Balance")).isEqualTo(0.0f);
        assertThat((String) getFieldValue("IBAN")).startsWith("DE");
    }

    @Test
    @DisplayName("Should correctly increase balance on valid positive deposit")
    void testDepositBalanceSuccess() throws Exception {
        float initialBalance = (float) getFieldValue("Balance");
        float depositAmount = 500.50f;

        assertThat(ac.depositBalance(depositAmount)).isTrue();

        assertThat((float) getFieldValue("Balance")).isEqualTo(initialBalance + depositAmount);
    }

    @Test
    @DisplayName("Should allow zero deposit without changing balance")
    void testDepositBalanceZero() throws Exception {
        float initialBalance = (float) getFieldValue("Balance");

        assertThat(ac.depositBalance(0.0f)).isTrue();

        assertThat((float) getFieldValue("Balance")).isEqualTo(initialBalance);
    }

    @Test
    @DisplayName("Should throw Exception when depositing a negative amount")
    void testDepositBalanceNegativeAmountThrowsException() throws Exception {
        float initialBalance = (float) getFieldValue("Balance");

        assertThatThrownBy(() -> ac.depositBalance(-100.00f))
                .isInstanceOf(Exception.class)
                .hasMessage("Amount must be positive");

        assertThat((float) getFieldValue("Balance")).isEqualTo(initialBalance);
    }

    @Test
    @DisplayName("Should successfully withdraw money when sufficient balance exists")
    void testWithdrawMoneySuccess() throws Exception {
        float initialBalance = (float) getFieldValue("Balance");
        float withdrawAmount = 2500.00f;

        assertThat(ac.withdrawMoney(withdrawAmount)).isTrue();

        assertThat((float) getFieldValue("Balance")).isEqualTo(initialBalance - withdrawAmount);
    }

    @Test
    @DisplayName("Should allow withdrawing exact total balance resulting in zero balance")
    void testWithdrawMoneyExactBalance() throws Exception {
        float initialBalance = (float) getFieldValue("Balance");

        assertThat(ac.withdrawMoney(initialBalance)).isTrue();

        assertThat((float) getFieldValue("Balance")).isEqualTo(0.0f);
    }

    @Test
    @DisplayName("Should throw Exception when withdrawing a negative amount")
    void testWithdrawMoneyNegativeAmountThrowsException() throws Exception {
        float initialBalance = (float) getFieldValue("Balance");

        assertThatThrownBy(() -> ac.withdrawMoney(-50.00f))
                .isInstanceOf(Exception.class)
                .hasMessage("Amount must be positive");

        assertThat((float) getFieldValue("Balance")).isEqualTo(initialBalance);
    }

    @Test
    @DisplayName("Should throw Exception when withdrawal amount exceeds available balance")
    void testWithdrawMoneyInsufficientFundsThrowsException() throws Exception {
        float initialBalance = (float) getFieldValue("Balance");

        assertThatThrownBy(() -> ac.withdrawMoney(initialBalance + 1.00f))
                .isInstanceOf(Exception.class)
                .hasMessage("Not enoug Money");

        assertThat((float) getFieldValue("Balance")).isEqualTo(initialBalance);
    }

    @Test
    @DisplayName("Should successfully change name when valid string is provided")
    void testChangeNameSuccess() throws Exception {
        String newName = "Erika Mustermann";

        assertThat(ac.changeName(newName)).isTrue();

        assertThat((String) getFieldValue("Name")).isEqualTo(newName);
    }

    @Test
    @DisplayName("Should throw Exception when new name is null")
    void testChangeNameNullThrowsException() throws Exception {
        String originalName = (String) getFieldValue("Name");

        assertThatThrownBy(() -> ac.changeName(null))
                .isInstanceOf(Exception.class)
                .hasMessage("Name cant be null");

        assertThat((String) getFieldValue("Name")).isEqualTo(originalName);
    }

    @Test
    @DisplayName("Should throw Exception when new name is empty or only whitespace")
    void testChangeNameEmptyOrBlankThrowsException() throws Exception {
        String originalName = (String) getFieldValue("Name");

        assertThatThrownBy(() -> ac.changeName(""))
                .isInstanceOf(Exception.class)
                .hasMessage("Name cant be empty");

        assertThatThrownBy(() -> ac.changeName("   "))
                .isInstanceOf(Exception.class)
                .hasMessage("Name cant be empty");

        assertThat((String) getFieldValue("Name")).isEqualTo(originalName);
    }

    private Object getFieldValue(String fieldName) throws Exception {
        Field field = Account.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(ac);
    }
}