package lab02;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankAccountTest {
    private BankAccount activeKycAccount;
    private BankAccount activeNoKycAccount;

    @BeforeEach
    public void setUp() {
        activeKycAccount = new BankAccount("ACC-123", 1000.0, true);
        activeNoKycAccount = new BankAccount("ACC-456", 1000.0, false);
    }

    // ==========================================
    // BLACKBOX TESTS
    // ==========================================

    @Test
    public void testConstructorValidation() {
        // Account number input validation
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(null, 500.0, true));
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("  ", 500.0, true));

        // Minimum balance compliance validation
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("ACC-789", 50.0, true));
        
        BankAccount validAccount = new BankAccount("ACC-789", 100.0, true);
        assertEquals("ACC-789", validAccount.getAccountNumber());
        assertEquals(100.0, validAccount.getBalance(), 0.0001);
    }

    @Test
    public void testDepositBusinessAndRegulatoryRules() {
        // Invalid deposit input validation
        assertThrows(IllegalArgumentException.class, () -> activeKycAccount.deposit(-50.0));
        assertThrows(IllegalArgumentException.class, () -> activeKycAccount.deposit(0.0));

        // Valid deposit correctness
        activeKycAccount.deposit(500.0);
        assertEquals(1500.0, activeKycAccount.getBalance(), 0.0001);

        // Regulatory Compliance: Deposit under 10000 limit with no KYC should succeed
        activeNoKycAccount.deposit(5000.0);
        assertEquals(6000.0, activeNoKycAccount.getBalance(), 0.0001);
        assertFalse(activeNoKycAccount.isFlaggedForAudit());

        // Regulatory Compliance: Deposit >= 10000 without KYC should throw exception and flag audit
        assertThrows(SecurityException.class, () -> activeNoKycAccount.deposit(10000.0));
        assertTrue(activeNoKycAccount.isFlaggedForAudit());

        // Regulatory Compliance: Deposit >= 10000 WITH KYC should succeed
        activeKycAccount.deposit(12000.0);
        assertEquals(13500.0, activeKycAccount.getBalance(), 0.0001);
        assertFalse(activeKycAccount.isFlaggedForAudit());
    }

    @Test
    public void testWithdrawalBusinessRules() {
        // Invalid withdraw input validation
        assertThrows(IllegalArgumentException.class, () -> activeKycAccount.withdraw(-50.0));
        assertThrows(IllegalArgumentException.class, () -> activeKycAccount.withdraw(0.0));

        // Normal withdraw under fee threshold ($2000)
        activeKycAccount.withdraw(500.0);
        assertEquals(500.0, activeKycAccount.getBalance(), 0.0001); // 1000 - 500

        // Withdraw over fee threshold ($2000) triggers $5 fee
        BankAccount largeAccount = new BankAccount("ACC-LARGE", 10000.0, true);
        largeAccount.withdraw(3000.0);
        assertEquals(6995.0, largeAccount.getBalance(), 0.0001); // 10000 - 3000 - 5 fee
    }

    @Test
    public void testWithdrawalDailyLimitAndMinBalance() {
        // Exceeding daily limit
        assertThrows(IllegalArgumentException.class, () -> activeKycAccount.withdraw(6000.0));

        // Multiple withdrawals exceeding limit
        BankAccount richAccount = new BankAccount("ACC-RICH", 10000.0, true);
        richAccount.withdraw(4000.0); // total = 4000, fee $5, balance = 5995
        assertThrows(IllegalArgumentException.class, () -> richAccount.withdraw(1500.0)); // total would be 5500 > 5000 limit

        // Withdraw violating minimum balance ($100)
        BankAccount acc = new BankAccount("ACC-MIN", 500.0, true);
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(450.0)); // 500 - 450 = 50 < 100

        // Withdraw and fee violating minimum balance
        BankAccount accFee = new BankAccount("ACC-FEE-MIN", 2200.0, true);
        // Withdraw 2098 -> fee 5 -> 2200 - 2098 - 5 = 97 < 100
        assertThrows(IllegalArgumentException.class, () -> accFee.withdraw(2098.0));
    }

    // ==========================================
    // WHITEBOX TESTS
    // ==========================================

    @Test
    public void testStateTransitionsAndGuards() {
        // Verify initial state
        assertEquals(BankAccount.AccountState.ACTIVE, activeKycAccount.getState());

        // SUSPEND account and check actions
        activeKycAccount.suspendAccount();
        assertEquals(BankAccount.AccountState.SUSPENDED, activeKycAccount.getState());

        assertThrows(IllegalStateException.class, () -> activeKycAccount.deposit(100.0));
        assertThrows(IllegalStateException.class, () -> activeKycAccount.withdraw(100.0));

        // Re-activate and check actions
        activeKycAccount.activateAccount();
        assertEquals(BankAccount.AccountState.ACTIVE, activeKycAccount.getState());
        activeKycAccount.deposit(100.0);
        assertEquals(1100.0, activeKycAccount.getBalance(), 0.0001);

        // CLOSE account and check actions
        activeKycAccount.closeAccount();
        assertEquals(BankAccount.AccountState.CLOSED, activeKycAccount.getState());

        assertThrows(IllegalStateException.class, () -> activeKycAccount.deposit(100.0));
        assertThrows(IllegalStateException.class, () -> activeKycAccount.withdraw(100.0));

        // Closed account cannot be activated or suspended
        assertThrows(IllegalStateException.class, () -> activeKycAccount.activateAccount());
        assertThrows(IllegalStateException.class, () -> activeKycAccount.suspendAccount());
    }

    @Test
    public void testResetDailyLimit() {
        BankAccount account = new BankAccount("ACC-LIMIT", 10000.0, true);
        account.withdraw(4000.0); // balance = 5995
        
        // Next withdrawal of 2000 would exceed 5000 daily limit (4000 + 2000 = 6000 > 5000)
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(2000.0));

        // Reset and try again
        account.resetDailyLimit();
        account.withdraw(2000.0); // balance = 5995 - 2000 = 3995 (no fee for exactly 2000)
        assertEquals(3995.0, account.getBalance(), 0.0001);
    }
}
