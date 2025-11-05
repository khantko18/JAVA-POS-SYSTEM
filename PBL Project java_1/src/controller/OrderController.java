package controller;

import model.*;
import view.OrderView;
import view.PaymentDialog;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Controller for managing orders
 */
public class OrderController {
    private MenuManager menuManager;
    private Order currentOrder;
    private SalesData salesData;
    private OrderView view;
    private int orderCounter;
    
    public OrderController(MenuManager menuManager, SalesData salesData, OrderView view) {
        this.menuManager = menuManager;
        this.salesData = salesData;
        this.view = view;
        this.orderCounter = 1;
        
        createNewOrder();
        initializeListeners();
        refreshMenuDisplay();
    }
    
    private void createNewOrder() {
        String orderId = String.format("ORD%04d", orderCounter++);
        currentOrder = new Order(orderId);
    }
    
    private void initializeListeners() {
        // Category filter
        view.getCategoryFilter().addActionListener(e -> refreshMenuDisplay());
        
        // Add item to order (from menu cards)
        Component[] menuCards = view.getMenuPanel().getComponents();
        for (Component card : menuCards) {
            if (card instanceof JPanel) {
                attachAddButtonListener((JPanel) card);
            }
        }
        
        // Apply discount button
        view.getApplyDiscountButton().addActionListener(e -> handleApplyDiscount());
        
        // Clear order button
        view.getClearButton().addActionListener(e -> handleClearOrder());
        
        // Payment button
        view.getPayButton().addActionListener(e -> handlePayment());
    }
    
    private void refreshMenuDisplay() {
        String selectedCategory = (String) view.getCategoryFilter().getSelectedItem();
        List<model.MenuItem> items;
        
        if ("All".equals(selectedCategory)) {
            items = menuManager.getAllMenuItems();
        } else {
            items = menuManager.getMenuItemsByCategory(selectedCategory);
        }
        
        view.displayMenuItems(items);
        
        // Re-attach listeners to new menu cards
        Component[] menuCards = view.getMenuPanel().getComponents();
        for (Component card : menuCards) {
            if (card instanceof JPanel) {
                attachAddButtonListener((JPanel) card);
            }
        }
    }
    
    private void attachAddButtonListener(JPanel card) {
        Component[] components = card.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                // Remove existing listeners
                for (java.awt.event.ActionListener al : button.getActionListeners()) {
                    button.removeActionListener(al);
                }
                // Add new listener
                button.addActionListener(e -> {
                    model.MenuItem item = (model.MenuItem) button.getClientProperty("menuItem");
                    handleAddItemToOrder(item);
                });
            }
        }
    }
    
    private void handleAddItemToOrder(model.MenuItem item) {
        if (item == null) {
            return;
        }
        
        String quantityStr = JOptionPane.showInputDialog(view,
            "Enter quantity for " + item.getName() + ":",
            "Quantity",
            JOptionPane.QUESTION_MESSAGE);
        
        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity > 0) {
                    currentOrder.addItem(item, quantity);
                    refreshOrderDisplay();
                } else {
                    JOptionPane.showMessageDialog(view,
                        "Quantity must be positive!",
                        "Invalid Quantity",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view,
                    "Invalid quantity format!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleApplyDiscount() {
        try {
            String discountStr = view.getDiscountField().getText().trim();
            double discount = Double.parseDouble(discountStr);
            
            if (discount < 0 || discount > 100) {
                JOptionPane.showMessageDialog(view,
                    "Discount must be between 0 and 100!",
                    "Invalid Discount",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentOrder.setDiscountPercent(discount);
            refreshOrderDisplay();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                "Invalid discount format!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleClearOrder() {
        int confirm = JOptionPane.showConfirmDialog(view,
            "Are you sure you want to clear the current order?",
            "Confirm Clear",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            currentOrder.clear();
            currentOrder.setDiscountPercent(0);
            view.getDiscountField().setText("0");
            refreshOrderDisplay();
        }
    }
    
    private void handlePayment() {
        if (currentOrder.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                "Cannot process payment for an empty order!",
                "Empty Order",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double totalAmount = currentOrder.getTotal();
        
        // Show payment dialog
        PaymentDialog paymentDialog = new PaymentDialog(
            (Frame) SwingUtilities.getWindowAncestor(view), 
            totalAmount
        );
        
        paymentDialog.getConfirmButton().addActionListener(e -> {
            try {
                if (paymentDialog.isCashPayment()) {
                    double received = paymentDialog.getAmountReceived();
                    if (received < totalAmount) {
                        JOptionPane.showMessageDialog(paymentDialog,
                            "Insufficient payment amount!",
                            "Payment Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                // Process payment
                String paymentId = String.format("PAY%04d", orderCounter);
                Payment.PaymentMethod method = paymentDialog.isCashPayment() ? 
                    Payment.PaymentMethod.CASH : Payment.PaymentMethod.CARD;
                
                Payment payment = new Payment(paymentId, currentOrder.getOrderId(), totalAmount, method);
                
                if (paymentDialog.isCashPayment()) {
                    payment.processCashPayment(paymentDialog.getAmountReceived());
                }
                
                // Record sale
                currentOrder.setStatus("Completed");
                salesData.recordSale(payment, currentOrder);
                
                // Show success message
                String message = "Payment successful!\n\n";
                message += "Order ID: " + currentOrder.getOrderId() + "\n";
                message += "Total: $" + String.format("%.2f", totalAmount) + "\n";
                
                if (paymentDialog.isCashPayment()) {
                    message += "Received: $" + String.format("%.2f", payment.getReceivedAmount()) + "\n";
                    message += "Change: $" + String.format("%.2f", payment.getChangeAmount());
                }
                
                JOptionPane.showMessageDialog(view,
                    message,
                    "Payment Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                paymentDialog.setConfirmed(true);
                paymentDialog.dispose();
                
                // Create new order
                createNewOrder();
                view.getDiscountField().setText("0");
                refreshOrderDisplay();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(paymentDialog,
                    "Payment error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        paymentDialog.setVisible(true);
    }
    
    private void refreshOrderDisplay() {
        view.updateOrderTable(currentOrder.getItems());
        view.updateTotals(
            currentOrder.getSubtotal(),
            currentOrder.getDiscountAmount(),
            currentOrder.getTotal()
        );
    }
    
    public Order getCurrentOrder() {
        return currentOrder;
    }
    
    public SalesData getSalesData() {
        return salesData;
    }
}

