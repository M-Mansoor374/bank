package services;

import models.Account;
import java.util.Map;
import java.util.HashMap;

/**
 * Account Service
 * Manages bank accounts
 */
public class AccountService {
    private static AccountService instance;
    private Map<String, Account> accounts; // accountId -> account
    private Map<String, String> usernameToAccountId; // username -> accountId

    private AccountService() {
        this.accounts = new HashMap<>();
        this.usernameToAccountId = new HashMap<>();
        initializeDefaultAccount();
    }

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    /**
     * Initialize default account matching frontend
     */
    private void initializeDefaultAccount() {
        // Create default account with initial balance of Rs 10,000
        Account account = new Account("ACC001", "user@gmail.com", 10000.0);
        accounts.put("ACC001", account);
        usernameToAccountId.put("user@gmail.com", "ACC001");
    }

    /**
     * Get account by account ID
     */
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    /**
     * Get account by username
     */
    public Account getAccountByUsername(String username) {
        String accountId = usernameToAccountId.get(username);
        if (accountId != null) {
            return accounts.get(accountId);
        }
        return null;
    }

    /**
     * Create a new account
     */
    public Account createAccount(String accountId, String username, double initialBalance) {
        Account account = new Account(accountId, username, initialBalance);
        accounts.put(accountId, account);
        usernameToAccountId.put(username, accountId);
        return account;
    }

    /**
     * Deposit money to account
     */
    public boolean deposit(String accountId, double amount) {
        Account account = accounts.get(accountId);
        if (account != null && amount > 0) {
            account.deposit(amount);
            return true;
        }
        return false;
    }

    /**
     * Withdraw money from account
     */
    public boolean withdraw(String accountId, double amount) {
        Account account = accounts.get(accountId);
        if (account != null && account.hasSufficientBalance(amount)) {
            return account.withdraw(amount);
        }
        return false;
    }

    /**
     * Transfer money between accounts
     */
    public boolean transfer(String fromAccountId, String toAccountId, double amount) {
        Account fromAccount = accounts.get(fromAccountId);
        Account toAccount = accounts.get(toAccountId);
        
        if (fromAccount == null || toAccount == null) {
            return false;
        }
        
        if (!fromAccount.hasSufficientBalance(amount)) {
            return false;
        }
        
        // Perform transfer
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        return true;
    }

    /**
     * Get account balance
     */
    public double getBalance(String accountId) {
        Account account = accounts.get(accountId);
        return account != null ? account.getBalance() : 0.0;
    }
}

