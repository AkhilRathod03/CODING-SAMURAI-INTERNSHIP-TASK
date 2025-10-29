import java.util.List;

/**
 * Represents the ATM machine.
 * It holds a list of accounts and provides methods for all ATM operations.
 */
public class ATM {
    private List<Account> accounts;
    private AccountService accountService;

    /**
     * Constructs a new ATM instance and initializes it with sample accounts.
     */
    public ATM() {
        accountService = new AccountService();
        accounts = accountService.loadAccounts();
    }

    /**
     * Authenticates a user based on account number and PIN.
     *
     * @param accountNumber The account number to log in with.
     * @param pin The PIN for the account.
     * @return The Account object if login is successful, null otherwise.
     */
    public Account login(String accountNumber, String pin) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber) && account.validatePin(pin)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Saves the accounts to the file.
     */
    public void saveAccounts() {
        accountService.saveAccounts(accounts);
    }
}
