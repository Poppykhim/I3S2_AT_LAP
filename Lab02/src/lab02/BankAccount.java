package lab02;

public class BankAccount {
    public enum AccountState {
        ACTIVE, SUSPENDED, CLOSED
    }

    private String accountNumber;
    private double balance;
    private AccountState state;
    private double dailyWithdrawalLimit;
    private double totalWithdrawnToday;
    private boolean kycVerified;
    private static final double MINIMUM_BALANCE = 100.0;
    private static final double REGULATORY_REPORTING_LIMIT = 10000.0;
    private boolean flaggedForAudit = false;

    public BankAccount(String accountNumber, double initialBalance, boolean kycVerified) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty");
        }
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException("Initial balance must be at least " + MINIMUM_BALANCE);
        }
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.kycVerified = kycVerified;
        this.state = AccountState.ACTIVE;
        this.dailyWithdrawalLimit = 5000.0;
        this.totalWithdrawnToday = 0.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public AccountState getState() {
        return state;
    }

    public boolean isKycVerified() {
        return kycVerified;
    }

    public boolean isFlaggedForAudit() {
        return flaggedForAudit;
    }

    public void setKycVerified(boolean kycVerified) {
        this.kycVerified = kycVerified;
    }

    public void suspendAccount() {
        if (this.state == AccountState.CLOSED) {
            throw new IllegalStateException("Cannot suspend a closed account");
        }
        this.state = AccountState.SUSPENDED;
    }

    public void activateAccount() {
        if (this.state == AccountState.CLOSED) {
            throw new IllegalStateException("Cannot activate a closed account");
        }
        this.state = AccountState.ACTIVE;
    }

    public void closeAccount() {
        this.state = AccountState.CLOSED;
    }

    public void resetDailyLimit() {
        this.totalWithdrawnToday = 0.0;
    }

    public void deposit(double amount) {
        if (state != AccountState.ACTIVE) {
            throw new IllegalStateException("Deposit not allowed. Account state is " + state);
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        if (amount >= REGULATORY_REPORTING_LIMIT) {
            if (!kycVerified) {
                this.flaggedForAudit = true;
                throw new SecurityException("Regulatory compliance: Large deposit requires KYC verification. Account flagged.");
            }
        }

        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (state != AccountState.ACTIVE) {
            throw new IllegalStateException("Withdrawal not allowed. Account state is " + state);
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (totalWithdrawnToday + amount > dailyWithdrawalLimit) {
            throw new IllegalArgumentException("Daily withdrawal limit of " + dailyWithdrawalLimit + " exceeded");
        }

        if (balance - amount < MINIMUM_BALANCE) {
            throw new IllegalArgumentException("Withdrawal would bring balance below minimum allowed balance: " + MINIMUM_BALANCE);
        }

        double fee = 0.0;
        if (amount > 2000.0) {
            fee = 5.0;
        }

        if (balance - amount - fee < MINIMUM_BALANCE) {
            throw new IllegalArgumentException("Withdrawal and regulatory fee would bring balance below minimum: " + MINIMUM_BALANCE);
        }

        this.balance -= (amount + fee);
        this.totalWithdrawnToday += amount;
    }
}
