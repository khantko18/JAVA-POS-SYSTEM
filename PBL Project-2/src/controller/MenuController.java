package controller;

import model.MenuItem;
import model.MenuManager;
import view.MenuManagementView;
import util.LanguageManager;
import javax.swing.*;

/**
 * Controller for managing menu items
 */
public class MenuController {
    private MenuManager menuManager;
    private MenuManagementView view;
    private LanguageManager langManager;
    
    public MenuController(MenuManager menuManager, MenuManagementView view) {
        this.menuManager = menuManager;
        this.view = view;
        this.langManager = LanguageManager.getInstance();
        
        initializeListeners();
        refreshMenuTable();
        setupLanguageListener();
    }
    
    private void setupLanguageListener() {
        langManager.addLanguageChangeListener(newLanguage -> {
            refreshMenuTable();
        });
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
            String displayCategory = (String) view.getCategoryCombo().getSelectedItem();
            String category = langManager.getCategoryKey(displayCategory);
            String priceText = view.getPriceField().getText().trim();
            String description = view.getDescriptionField().getText().trim();
            
            // Validation
            if (name.isEmpty()) {
                throw new IllegalArgumentException(langManager.getText("name_empty"));
            }
            
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new IllegalArgumentException(langManager.getText("price_positive"));
            }
            
            // Create and add new item
            String newId = menuManager.generateNewId();
            MenuItem newItem = new MenuItem(newId, name, category, price, description);
            menuManager.addMenuItem(newItem);
            
            // Refresh and clear
            refreshMenuTable();
            view.clearForm();
            
            JOptionPane.showMessageDialog(view, 
                langManager.getText("item_added"), 
                langManager.getText("success"), 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, 
                langManager.getText("invalid_price_format"), 
                langManager.getText("error"), 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, 
                ex.getMessage(), 
                langManager.getText("validation_error"), 
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
                    langManager.getText("error"), 
                    langManager.getText("error"), 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String name = view.getNameField().getText().trim();
            String displayCategory = (String) view.getCategoryCombo().getSelectedItem();
            String category = langManager.getCategoryKey(displayCategory);
            String priceText = view.getPriceField().getText().trim();
            String description = view.getDescriptionField().getText().trim();
            
            // Validation
            if (name.isEmpty()) {
                throw new IllegalArgumentException(langManager.getText("name_empty"));
            }
            
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new IllegalArgumentException(langManager.getText("price_positive"));
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
                langManager.getText("item_updated"), 
                langManager.getText("success"), 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, 
                langManager.getText("invalid_price_format"), 
                langManager.getText("error"), 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, 
                ex.getMessage(), 
                langManager.getText("validation_error"), 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleDeleteItem() {
        String itemId = view.getSelectedItemId();
        if (itemId == null) {
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(view,
            langManager.getText("confirm_delete"),
            langManager.getText("confirm_delete_title"),
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            menuManager.removeMenuItem(itemId);
            refreshMenuTable();
            view.clearForm();
            
            JOptionPane.showMessageDialog(view, 
                langManager.getText("item_deleted"), 
                langManager.getText("success"), 
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

