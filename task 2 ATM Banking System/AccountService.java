
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class AccountService {
    private static final String ACCOUNTS_FILE = "accounts.txt";

    public List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>();
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) {
            createDefaultAccountsFile();
        }

        try (InputStream input = new FileInputStream(ACCOUNTS_FILE)) {
            Properties props = new Properties();
            props.load(input);

            for (String accountNumber : props.stringPropertyNames()) {
                String[] accountData = props.getProperty(accountNumber).split(",");
                String pin = new String(Base64.getDecoder().decode(accountData[0]));
                double balance = Double.parseDouble(accountData[1]);
                Account account = new Account(accountNumber, pin, balance);
                accounts.add(account);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return accounts;
    }

    public void saveAccounts(List<Account> accounts) {
        Properties props = new Properties();
        for (Account account : accounts) {
            String pin = Base64.getEncoder().encodeToString(account.getPin().getBytes());
            props.setProperty(account.getAccountNumber(), pin + "," + account.getBalance());
        }

        try (OutputStream output = new FileOutputStream(ACCOUNTS_FILE)) {
            props.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void createDefaultAccountsFile() {
        Properties props = new Properties();
        // Default accounts: 123456 (pin: 1234), 654321 (pin: 4321)
        props.setProperty("123456", Base64.getEncoder().encodeToString("1234".getBytes()) + ",1000.0");
        props.setProperty("654321", Base64.getEncoder().encodeToString("4321".getBytes()) + ",500.0");

        try (OutputStream output = new FileOutputStream(ACCOUNTS_FILE)) {
            props.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
