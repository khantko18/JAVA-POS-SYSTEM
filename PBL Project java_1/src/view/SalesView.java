package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * View for displaying sales statistics
 */
public class SalesView extends JPanel {
    private JLabel totalRevenueLabel;
    private JLabel todaySalesLabel;
    private JLabel todayOrdersLabel;
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JTable itemStatsTable;
    private DefaultTableModel itemStatsModel;
    
    public SalesView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Top panel - Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        // Total Revenue Card
        JPanel revenueCard = createStatCard("Total Revenue", "$0.00", new Color(40, 167, 69));
        totalRevenueLabel = (JLabel) ((JPanel) revenueCard.getComponent(1)).getComponent(0);
        statsPanel.add(revenueCard);
        
        // Today's Sales Card
        JPanel todaySalesCard = createStatCard("Today's Sales", "$0.00", new Color(0, 123, 255));
        todaySalesLabel = (JLabel) ((JPanel) todaySalesCard.getComponent(1)).getComponent(0);
        statsPanel.add(todaySalesCard);
        
        // Today's Orders Card
        JPanel todayOrdersCard = createStatCard("Today's Orders", "0", new Color(255, 193, 7));
        todayOrdersLabel = (JLabel) ((JPanel) todayOrdersCard.getComponent(1)).getComponent(0);
        statsPanel.add(todayOrdersCard);
        
        // Center panel - Tables split
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Recent sales table
        JPanel salesPanel = new JPanel(new BorderLayout());
        salesPanel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));
        
        String[] salesColumns = {"Time", "Order ID", "Amount", "Payment"};
        tableModel = new DefaultTableModel(salesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        salesTable = new JTable(tableModel);
        salesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        salesTable.setRowHeight(25);
        JScrollPane salesScroll = new JScrollPane(salesTable);
        salesPanel.add(salesScroll, BorderLayout.CENTER);
        
        // Item statistics table
        JPanel itemStatsPanel = new JPanel(new BorderLayout());
        itemStatsPanel.setBorder(BorderFactory.createTitledBorder("Popular Items"));
        
        String[] itemColumns = {"Item Name", "Quantity Sold"};
        itemStatsModel = new DefaultTableModel(itemColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemStatsTable = new JTable(itemStatsModel);
        itemStatsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        itemStatsTable.setRowHeight(25);
        JScrollPane itemStatsScroll = new JScrollPane(itemStatsTable);
        itemStatsPanel.add(itemStatsScroll, BorderLayout.CENTER);
        
        centerPanel.add(salesPanel);
        centerPanel.add(itemStatsPanel);
        
        // Add panels to main view
        add(statsPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.DARK_GRAY);
        
        JPanel valuePanel = new JPanel();
        valuePanel.setOpaque(false);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valuePanel.add(valueLabel);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void updateStatistics(double totalRevenue, double todaySales, int todayOrders) {
        totalRevenueLabel.setText(String.format("$%.2f", totalRevenue));
        todaySalesLabel.setText(String.format("$%.2f", todaySales));
        todayOrdersLabel.setText(String.valueOf(todayOrders));
    }
    
    public void updateSalesTable(java.util.List<String[]> salesData) {
        tableModel.setRowCount(0);
        for (String[] row : salesData) {
            tableModel.addRow(row);
        }
    }
    
    public void updateItemStats(Map<String, Integer> itemStats) {
        itemStatsModel.setRowCount(0);
        
        // Sort by quantity descending
        itemStats.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(entry -> {
                Object[] row = {entry.getKey(), entry.getValue()};
                itemStatsModel.addRow(row);
            });
    }
}

