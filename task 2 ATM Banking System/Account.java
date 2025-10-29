
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank account.
 * Each account has an account number, a PIN, a balance, and a transaction history.
 */
public class Account {
    private String accountNumber;
    private String pin;
    private double balance;
    private List<String> transactionHistory;
    private final String transactionFile;

    /**
     * Constructs a new Account instance.
     *
     * @param accountNumber The unique identifier for the account.
     * @param pin The personal identification number for authentication.
     * @param balance The initial balance of the account.
     */
    public Account(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
        this.transactionFile = accountNumber + "_transactions.txt";
        loadTransactionHistory();
        if (transactionHistory.isEmpty()) {
            addTransaction("Initial balance: " + balance);
        }
    }

    /**
     * Returns the account number.
     *
     * @return The account number.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Validates the provided PIN.
     *
     * @param pin The PIN to validate.
     * @return True if the PIN is correct, false otherwise.
     */
    public boolean validatePin(String pin) {
        return this.pin.equals(pin);
    }

    /**
     * Returns the current balance of the account.
     *
     * @return The current balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Deposits a specified amount into the account.
     *
     * @param amount The amount to deposit.
     */
    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposited: " + amount);
    }

    /**
     * Withdraws a specified amount from the account.
     *
     * @param amount The amount to withdraw.
     * @return True if the withdrawal was successful, false otherwise.
     */
    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            addTransaction("Withdrew: " + amount);
            return true;
        }
        return false;
    }

    /**
     * Returns the transaction history for the account.
     *
     * @return A list of transaction records.
     */
    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Adds a transaction record to the history and saves it to the transaction file.
     *
     * @param transaction The transaction details to add.
     */
    private void addTransaction(String transaction) {
        transactionHistory.add(transaction);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionFile, true))) {
            writer.write(transaction);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the transaction history from the transaction file.
     */
    private void loadTransactionHistory() {
        File file = new File(transactionFile);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    transactionHistory.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets a new PIN for the account.
     *
     * @param newPin The new PIN to set.
     */
    public void setPin(String newPin) {
        this.pin = newPin;
    }

    /**
     * Returns the PIN.
     *
     * @return The PIN.
     */
    public String getPin() {
        return pin;
    }
}
