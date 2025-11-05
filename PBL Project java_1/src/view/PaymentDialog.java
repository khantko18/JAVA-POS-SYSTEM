package view;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for processing payments
 */
public class PaymentDialog extends JDialog {
    private JRadioButton cashRadio;
    private JRadioButton cardRadio;
    private JTextField amountReceivedField;
    private JLabel changeLabel;
    private JButton confirmButton;
    private JButton cancelButton;
    private double totalAmount;
    private boolean confirmed;
    
    public PaymentDialog(Frame parent, double totalAmount) {
        super(parent, "Process Payment", true);
        this.totalAmount = totalAmount;
        this.confirmed = false;
        
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Center panel - Payment details
        JPanel centerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Total amount
        centerPanel.add(new JLabel("Total Amount:"));
        JLabel totalLabel = new JLabel(String.format("$%.2f", totalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 128, 0));
        centerPanel.add(totalLabel);
        
        // Payment method
        centerPanel.add(new JLabel("Payment Method:"));
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cashRadio = new JRadioButton("Cash", true);
        cardRadio = new JRadioButton("Card");
        ButtonGroup methodGroup = new ButtonGroup();
        methodGroup.add(cashRadio);
        methodGroup.add(cardRadio);
        methodPanel.add(cashRadio);
        methodPanel.add(cardRadio);
        centerPanel.add(methodPanel);
        
        // Amount received (for cash)
        centerPanel.add(new JLabel("Amount Received:"));
        amountReceivedField = new JTextField();
        amountReceivedField.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(amountReceivedField);
        
        // Change
        centerPanel.add(new JLabel("Change:"));
        changeLabel = new JLabel("$0.00");
        changeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        centerPanel.add(changeLabel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        confirmButton = new JButton("Confirm Payment");
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setPreferredSize(new Dimension(160, 40));
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add listeners
        cashRadio.addActionListener(e -> amountReceivedField.setEnabled(true));
        cardRadio.addActionListener(e -> {
            amountReceivedField.setEnabled(false);
            amountReceivedField.setText("");
            changeLabel.setText("$0.00");
        });
        
        amountReceivedField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculateChange();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void calculateChange() {
        try {
            double received = Double.parseDouble(amountReceivedField.getText());
            double change = received - totalAmount;
            changeLabel.setText(String.format("$%.2f", Math.max(0, change)));
        } catch (NumberFormatException ex) {
            changeLabel.setText("$0.00");
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    
    public boolean isCashPayment() {
        return cashRadio.isSelected();
    }
    
    public double getAmountReceived() {
        try {
            return Double.parseDouble(amountReceivedField.getText());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    public JButton getConfirmButton() {
        return confirmButton;
    }
    
    public JButton getCancelButton() {
        return cancelButton;
    }
}

