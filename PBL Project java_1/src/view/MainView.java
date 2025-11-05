package view;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window with tabbed interface
 */
public class MainView extends JFrame {
    private JTabbedPane tabbedPane;
    private OrderView orderView;
    private MenuManagementView menuManagementView;
    private SalesView salesView;
    
    public MainView() {
        setTitle("Cafe POS System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeComponents();
        applyModernStyling();
    }
    
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Create views
        orderView = new OrderView();
        menuManagementView = new MenuManagementView();
        salesView = new SalesView();
        
        // Add tabs
        tabbedPane.addTab("📋 Order", orderView);
        tabbedPane.addTab("☕ Menu Management", menuManagementView);
        tabbedPane.addTab("📊 Sales Statistics", salesView);
        
        add(tabbedPane);
    }
    
    private void applyModernStyling() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public OrderView getOrderView() {
        return orderView;
    }
    
    public MenuManagementView getMenuManagementView() {
        return menuManagementView;
    }
    
    public SalesView getSalesView() {
        return salesView;
    }
}

