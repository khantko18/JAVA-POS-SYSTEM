package view;

import model.MenuItem;
import model.OrderItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * View for processing customer orders
 */
public class OrderView extends JPanel {
    private JPanel menuPanel;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JTextField discountField;
    private JButton applyDiscountButton;
    private JButton payButton;
    private JButton clearButton;
    private JComboBox<String> categoryFilter;
    
    public OrderView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Left side - Menu items in card format
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        
        // Category filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Category:"));
        categoryFilter = new JComboBox<>(new String[]{"All", "Coffee", "Beverage", "Dessert"});
        filterPanel.add(categoryFilter);
        leftPanel.add(filterPanel, BorderLayout.NORTH);
        
        // Menu items panel with scroll
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 2, 10, 10));
        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.getVerticalScrollBar().setUnitIncrement(16);
        leftPanel.add(menuScroll, BorderLayout.CENTER);
        
        // Right side - Order details
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Current Order"));
        rightPanel.setPreferredSize(new Dimension(400, 0));
        
        // Order table
        String[] columns = {"Item", "Qty", "Price", "Subtotal"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setFont(new Font("Arial", Font.PLAIN, 12));
        orderTable.setRowHeight(25);
        JScrollPane tableScroll = new JScrollPane(orderTable);
        rightPanel.add(tableScroll, BorderLayout.CENTER);
        
        // Bottom panel with totals and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        
        // Totals panel
        JPanel totalsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        totalsPanel.add(new JLabel("Discount (%):"));
        JPanel discountInputPanel = new JPanel(new BorderLayout(5, 0));
        discountField = new JTextField("0");
        applyDiscountButton = new JButton("Apply");
        discountInputPanel.add(discountField, BorderLayout.CENTER);
        discountInputPanel.add(applyDiscountButton, BorderLayout.EAST);
        totalsPanel.add(discountInputPanel);
        
        totalsPanel.add(new JLabel("Subtotal:"));
        subtotalLabel = new JLabel("$0.00");
        subtotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalsPanel.add(subtotalLabel);
        
        totalsPanel.add(new JLabel("Discount:"));
        discountLabel = new JLabel("$0.00");
        discountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalsPanel.add(discountLabel);
        
        totalsPanel.add(new JLabel("Total:"));
        totalLabel = new JLabel("$0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 128, 0));
        totalsPanel.add(totalLabel);
        
        bottomPanel.add(totalsPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        clearButton = new JButton("Clear Order");
        clearButton.setBackground(new Color(220, 53, 69));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        payButton = new JButton("Proceed to Payment");
        payButton.setBackground(new Color(40, 167, 69));
        payButton.setForeground(Color.BLACK);
        payButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        buttonsPanel.add(clearButton);
        buttonsPanel.add(payButton);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add panels to main view
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    public void displayMenuItems(List<MenuItem> menuItems) {
        menuPanel.removeAll();
        
        for (MenuItem item : menuItems) {
            JPanel card = createMenuCard(item);
            menuPanel.add(card);
        }
        
        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private JPanel createMenuCard(MenuItem item) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        
        // Item details
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 0, 2));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel categoryLabel = new JLabel(item.getCategory());
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        categoryLabel.setForeground(Color.GRAY);
        
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", item.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 13));
        priceLabel.setForeground(new Color(0, 128, 0));
        
        detailsPanel.add(nameLabel);
        detailsPanel.add(categoryLabel);
        detailsPanel.add(priceLabel);
        
        // Add button
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0, 123, 255));
        addButton.setForeground(Color.BLACK);
        addButton.setFocusPainted(false);
        addButton.putClientProperty("menuItem", item);
        
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.SOUTH);
        
        return card;
    }
    
    public void updateOrderTable(List<OrderItem> items) {
        tableModel.setRowCount(0);
        
        for (OrderItem item : items) {
            Object[] row = {
                item.getMenuItem().getName(),
                item.getQuantity(),
                String.format("$%.2f", item.getMenuItem().getPrice()),
                String.format("$%.2f", item.getSubtotal())
            };
            tableModel.addRow(row);
        }
    }
    
    public void updateTotals(double subtotal, double discount, double total) {
        subtotalLabel.setText(String.format("$%.2f", subtotal));
        discountLabel.setText(String.format("$%.2f", discount));
        totalLabel.setText(String.format("$%.2f", total));
    }
    
    // Getters for UI components
    public JPanel getMenuPanel() { return menuPanel; }
    public JTable getOrderTable() { return orderTable; }
    public JTextField getDiscountField() { return discountField; }
    public JButton getApplyDiscountButton() { return applyDiscountButton; }
    public JButton getPayButton() { return payButton; }
    public JButton getClearButton() { return clearButton; }
    public JComboBox<String> getCategoryFilter() { return categoryFilter; }
}

