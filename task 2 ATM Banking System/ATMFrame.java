
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ATMFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Account currentAccount;
    private JPasswordField oldPinField;
    private JPasswordField newPinField;
    private JPasswordField confirmPinField;
    private JButton twentyButton, fortyButton, sixtyButton, hundredButton, changePinButton, backButton;
    private ATM atm;

    public ATMFrame() {
        setTitle("ATM Banking System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        atm = new ATM();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                atm.saveAccounts();
                super.windowClosing(e);
            }
        });

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Welcome Screen
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel logoLabel = new JLabel(new ImageIcon("logo.png"));
        welcomePanel.add(logoLabel, BorderLayout.CENTER);
        JButton startButton = new JButton("Start");
        welcomePanel.add(startButton, BorderLayout.SOUTH);

        cardPanel.add(welcomePanel, "welcome");

        // Login Screen
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel accountLabel = new JLabel("Account Number:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(accountLabel, gbc);

        JTextField accountField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(accountField, gbc);

        JLabel pinLabel = new JLabel("PIN:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(pinLabel, gbc);

        JPasswordField pinField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(pinField, gbc);

        JPanel keypadPanel = new JPanel(new GridLayout(4, 3));
        for (int i = 1; i <= 9; i++) {
            keypadPanel.add(new JButton(String.valueOf(i)));
        }
        keypadPanel.add(new JButton("Clear"));
        keypadPanel.add(new JButton("0"));
        keypadPanel.add(new JButton("Enter"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(keypadPanel, gbc);

        for (Component comp : keypadPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String command = e.getActionCommand();
                        if ("Clear".equals(command)) {
                            if (accountField.isFocusOwner()) {
                                accountField.setText("");
                            } else if (pinField.isFocusOwner()) {
                                pinField.setText("");
                            }
                        } else if ("Enter".equals(command)) {
                            currentAccount = atm.login(accountField.getText(), new String(pinField.getPassword()));
                            if (currentAccount != null) {
                                cardLayout.show(cardPanel, "mainMenu");
                            } else {
                                JOptionPane.showMessageDialog(ATMFrame.this, "Invalid account number or PIN.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            if (accountField.isFocusOwner()) {
                                accountField.setText(accountField.getText() + command);
                            } else if (pinField.isFocusOwner()) {
                                pinField.setText(new String(pinField.getPassword()) + command);
                            }
                        }
                    }
                });
            }
        }

        cardPanel.add(loginPanel, "login");

        // Main Menu Screen
        JPanel mainMenuPanel = new JPanel(new GridLayout(4, 2));
        mainMenuPanel.add(new JButton("Check Balance"));
        mainMenuPanel.add(new JButton("Deposit"));
        mainMenuPanel.add(new JButton("Withdraw"));
        mainMenuPanel.add(new JButton("Mini Statement"));
        mainMenuPanel.add(new JButton("Fast Cash"));
        mainMenuPanel.add(new JButton("Change PIN"));
        mainMenuPanel.add(new JButton("Exit"));

        cardPanel.add(mainMenuPanel, "mainMenu");

        // Fast Cash Screen
        JPanel fastCashPanel = new JPanel(new GridLayout(2, 2));
        twentyButton = new JButton("₹100");
        fastCashPanel.add(twentyButton);
        fortyButton = new JButton("₹500");
        fastCashPanel.add(fortyButton);
        sixtyButton = new JButton("₹1000");
        fastCashPanel.add(sixtyButton);
        hundredButton = new JButton("₹2000");
        fastCashPanel.add(hundredButton);
        cardPanel.add(fastCashPanel, "fastCash");

        // Change PIN Screen
        JPanel changePinPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcPin = new GridBagConstraints();
        gbcPin.insets = new Insets(5, 5, 5, 5);

        JLabel oldPinLabel = new JLabel("Old PIN:");
        gbcPin.gridx = 0;
        gbcPin.gridy = 0;
        changePinPanel.add(oldPinLabel, gbcPin);

        oldPinField = new JPasswordField(15);
        gbcPin.gridx = 1;
        gbcPin.gridy = 0;
        changePinPanel.add(oldPinField, gbcPin);

        JLabel newPinLabel = new JLabel("New PIN:");
        gbcPin.gridx = 0;
        gbcPin.gridy = 1;
        changePinPanel.add(newPinLabel, gbcPin);

        newPinField = new JPasswordField(15);
        gbcPin.gridx = 1;
        gbcPin.gridy = 1;
        changePinPanel.add(newPinField, gbcPin);

        JLabel confirmPinLabel = new JLabel("Confirm New PIN:");
        gbcPin.gridx = 0;
        gbcPin.gridy = 2;
        changePinPanel.add(confirmPinLabel, gbcPin);

        confirmPinField = new JPasswordField(15);
        gbcPin.gridx = 1;
        gbcPin.gridy = 2;
        changePinPanel.add(confirmPinField, gbcPin);

        changePinButton = new JButton("Change PIN");
        gbcPin.gridx = 1;
        gbcPin.gridy = 3;
        changePinPanel.add(changePinButton, gbcPin);
        
        backButton = new JButton("Back to Main Menu");
        gbcPin.gridx = 0;
        gbcPin.gridy = 3;
        changePinPanel.add(backButton, gbcPin);

        cardPanel.add(changePinPanel, "changePin");

        for (Component comp : mainMenuPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String command = e.getActionCommand();
                        if ("Exit".equals(command)) {
                            int choice = JOptionPane.showConfirmDialog(ATMFrame.this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                            if (choice == JOptionPane.YES_OPTION) {
                                atm.saveAccounts();
                                System.exit(0);
                            }
                        } else if ("Check Balance".equals(command)) {
                            if (currentAccount != null) {
                                JOptionPane.showMessageDialog(ATMFrame.this, "Current balance: " + currentAccount.getBalance(), "Balance", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else if ("Deposit".equals(command)) {
                            if (currentAccount != null) {
                                String amountString = JOptionPane.showInputDialog(ATMFrame.this, "Enter amount to deposit:", "Deposit", JOptionPane.PLAIN_MESSAGE);
                                if (amountString != null && !amountString.isEmpty()) {
                                    try {
                                        double amount = Double.parseDouble(amountString);
                                        if (amount > 0) {
                                            currentAccount.deposit(amount);
                                            JOptionPane.showMessageDialog(ATMFrame.this, "Deposit successful. New balance: " + currentAccount.getBalance(), "Success", JOptionPane.INFORMATION_MESSAGE);
                                        } else {
                                            JOptionPane.showMessageDialog(ATMFrame.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog(ATMFrame.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        } else if ("Withdraw".equals(command)) {
                            if (currentAccount != null) {
                                String amountString = JOptionPane.showInputDialog(ATMFrame.this, "Enter amount to withdraw:", "Withdraw", JOptionPane.PLAIN_MESSAGE);
                                if (amountString != null && !amountString.isEmpty()) {
                                    try {
                                        double amount = Double.parseDouble(amountString);
                                        if (amount > 0) {
                                            if (currentAccount.withdraw(amount)) {
                                                JOptionPane.showMessageDialog(ATMFrame.this, "Withdrawal successful. New balance: " + currentAccount.getBalance(), "Success", JOptionPane.INFORMATION_MESSAGE);
                                            } else {
                                                JOptionPane.showMessageDialog(ATMFrame.this, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(ATMFrame.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog(ATMFrame.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        } else if ("Mini Statement".equals(command)) {
                            if (currentAccount != null) {
                                StringBuilder statement = new StringBuilder("--- Mini Statement ---\n");
                                for (String transaction : currentAccount.getTransactionHistory()) {
                                    statement.append(transaction).append("\n");
                                }
                                statement.append("---------------------\n");
                                JOptionPane.showMessageDialog(ATMFrame.this, statement.toString(), "Mini Statement", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else if ("Fast Cash".equals(command)) {
                            cardLayout.show(cardPanel, "fastCash");
                        } else if ("Change PIN".equals(command)) {
                            cardLayout.show(cardPanel, "changePin");
                        } else {
                            JOptionPane.showMessageDialog(ATMFrame.this, command + " feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                });
            }
        }

        ActionListener fastCashListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAccount != null) {
                    String amountString = ((JButton) e.getSource()).getText().substring(1);
                    double amount = Double.parseDouble(amountString);
                    if (currentAccount.withdraw(amount)) {
                        JOptionPane.showMessageDialog(ATMFrame.this, "Withdrawal successful. New balance: " + currentAccount.getBalance(), "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ATMFrame.this, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    cardLayout.show(cardPanel, "mainMenu");
                }
            }
        };

        twentyButton.addActionListener(fastCashListener);
        fortyButton.addActionListener(fastCashListener);
        sixtyButton.addActionListener(fastCashListener);
        hundredButton.addActionListener(fastCashListener);

        changePinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAccount != null) {
                    String oldPin = new String(oldPinField.getPassword());
                    String newPin = new String(newPinField.getPassword());
                    String confirmPin = new String(confirmPinField.getPassword());

                    if (currentAccount.validatePin(oldPin)) {
                        if (newPin.equals(confirmPin)) {
                            currentAccount.setPin(newPin);
                            JOptionPane.showMessageDialog(ATMFrame.this, "PIN changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            cardLayout.show(cardPanel, "mainMenu");
                        } else {
                            JOptionPane.showMessageDialog(ATMFrame.this, "New PINs do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(ATMFrame.this, "Incorrect old PIN.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "mainMenu");
            }
        });

        add(cardPanel);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "login");
            }
        });

        cardLayout.show(cardPanel, "welcome");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ATMFrame().setVisible(true);
            }
        });
    }
}
