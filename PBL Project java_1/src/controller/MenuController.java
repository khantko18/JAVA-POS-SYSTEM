package controller;

import model.MenuItem;
import model.MenuManager;
import view.MenuManagementView;
import javax.swing.*;

/**
 * Controller for managing menu items
 */
public class MenuController {
    private MenuManager menuManager;
    private MenuManagementView view;
    
    public MenuController(MenuManager menuManager, MenuManagementView view) {
        this.menuManager = menuManager;
        this.view = view;
        
        initializeListeners();
        refreshMenuTable();
    }
    
    private void initializeListeners() {
        // Table selection listener
        view.getMenuTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection();
            }
        });
        
        // Add button
        view.getAddButton().addActionListener(e -> handleAddItem());
        
        // Update button
        view.getUpdateButton().addActionListener(e -> handleUpdateItem());
        
        // Delete button
        view.getDeleteButton().addActionListener(e -> handleDeleteItem());
        
        // Clear button
        view.getClearButton().addActionListener(e -> view.clearForm());
    }
    
    private void handleTableSelection() {
        int selectedRow = view.getMenuTable().getSelectedRow();
        if (selectedRow >= 0) {
            String itemId = (String) view.getMenuTable().getValueAt(selectedRow, 0);
            MenuItem item = menuManager.getMenuItem(itemId);
            if (item != null) {
                view.loadItemToForm(item);
            }
        }
    }
    
    private void handleAddItem() {
        try {
            String name = view.getNameField().getText().trim();
            String category = (String) view.getCategoryCombo().getSelectedItem();
            String priceText = view.getPriceField().getText().trim();
            String description = view.getDescriptionField().getText().trim();
            
            // Validation
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            
            // Create and add new item
            String newId = menuManager.generateNewId();
            MenuItem newItem = new MenuItem(newId, name, category, price, description);
            menuManager.addMenuItem(newItem);
            
            // Refresh and clear
            refreshMenuTable();
            view.clearForm();
            
            JOptionPane.showMessageDialog(view, 
                "Menu item added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, 
                "Invalid price format. Please enter a valid number.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, 
                ex.getMessage(), 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleUpdateItem() {
        try {
            String itemId = view.getSelectedItemId();
            if (itemId == null) {
                return;
            }
            
            MenuItem item = menuManager.getMenuItem(itemId);
            if (item == null) {
                JOptionPane.showMessageDialog(view, 
                    "Item not found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String name = view.getNameField().getText().trim();
            String category = (String) view.getCategoryCombo().getSelectedItem();
            String priceText = view.getPriceField().getText().trim();
            String description = view.getDescriptionField().getText().trim();
            
            // Validation
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            
            // Update item
            item.setName(name);
            item.setCategory(category);
            item.setPrice(price);
            item.setDescription(description);
            
            // Refresh and clear
            refreshMenuTable();
            view.clearForm();
            
            JOptionPane.showMessageDialog(view, 
                "Menu item updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, 
                "Invalid price format. Please enter a valid number.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, 
                ex.getMessage(), 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleDeleteItem() {
        String itemId = view.getSelectedItemId();
        if (itemId == null) {
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(view,
            "Are you sure you want to delete this item?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            menuManager.removeMenuItem(itemId);
            refreshMenuTable();
            view.clearForm();
            
            JOptionPane.showMessageDialog(view, 
                "Menu item deleted successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void refreshMenuTable() {
        view.displayMenuItems(menuManager.getAllMenuItems());
    }
    
    public MenuManager getMenuManager() {
        return menuManager;
    }
}

