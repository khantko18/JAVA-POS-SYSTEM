package view;

import model.MenuItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * View for managing menu items (add, edit, delete)
 */
public class MenuManagementView extends JPanel {
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField descriptionField;
    private JComboBox<String> categoryCombo;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private String selectedItemId;
    
    public MenuManagementView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Top panel - Menu table
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        
        String[] columns = {"ID", "Name", "Category", "Price", "Description", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(tableModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuTable.setFont(new Font("Arial", Font.PLAIN, 12));
        menuTable.setRowHeight(25);
        
        JScrollPane tableScroll = new JScrollPane(menuTable);
        topPanel.add(tableScroll, BorderLayout.CENTER);
        
        // Bottom panel - Form for adding/editing
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Add/Edit Menu Item"));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Category:"));
        categoryCombo = new JComboBox<>(new String[]{"Coffee", "Beverage", "Dessert", "Food"});
        formPanel.add(categoryCombo);
        
        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);
        
        formPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        formPanel.add(descriptionField);
        
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Add New Item");
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        updateButton = new JButton("Update Item");
        updateButton.setBackground(new Color(255, 193, 7));
        updateButton.setForeground(Color.BLACK);
        updateButton.setFont(new Font("Arial", Font.BOLD, 12));
        updateButton.setEnabled(false);
        
        deleteButton = new JButton("Delete Item");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.setEnabled(false);
        
        clearButton = new JButton("Clear Form");
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);
        
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Add panels to main view
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void displayMenuItems(List<MenuItem> items) {
        tableModel.setRowCount(0);
        
        for (MenuItem item : items) {
            Object[] row = {
                item.getId(),
                item.getName(),
                item.getCategory(),
                String.format("$%.2f", item.getPrice()),
                item.getDescription(),
                item.isAvailable() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }
    
    public void clearForm() {
        nameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
        categoryCombo.setSelectedIndex(0);
        selectedItemId = null;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        addButton.setEnabled(true);
    }
    
    public void loadItemToForm(MenuItem item) {
        nameField.setText(item.getName());
        priceField.setText(String.valueOf(item.getPrice()));
        descriptionField.setText(item.getDescription());
        categoryCombo.setSelectedItem(item.getCategory());
        selectedItemId = item.getId();
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
        addButton.setEnabled(false);
    }
    
    // Getters
    public JTable getMenuTable() { return menuTable; }
    public JTextField getNameField() { return nameField; }
    public JTextField getPriceField() { return priceField; }
    public JTextField getDescriptionField() { return descriptionField; }
    public JComboBox<String> getCategoryCombo() { return categoryCombo; }
    public JButton getAddButton() { return addButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getClearButton() { return clearButton; }
    public String getSelectedItemId() { return selectedItemId; }
}

