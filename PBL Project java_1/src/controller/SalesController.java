package controller;

import model.Payment;
import model.SalesData;
import view.SalesView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing sales statistics
 */
public class SalesController {
    private SalesData salesData;
    private SalesView view;
    private DateTimeFormatter timeFormatter;
    
    public SalesController(SalesData salesData, SalesView view) {
        this.salesData = salesData;
        this.view = view;
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        refreshStatistics();
    }
    
    public void refreshStatistics() {
        LocalDate today = LocalDate.now();
        
        // Calculate statistics
        double totalRevenue = salesData.getTotalRevenue();
        double todaySales = salesData.getTotalSales(today);
        int todayOrders = salesData.getTotalOrders(today);
        
        // Update view
        view.updateStatistics(totalRevenue, todaySales, todayOrders);
        
        // Update sales table
        List<Payment> todayPayments = salesData.getSalesByDate(today);
        List<String[]> salesTableData = new ArrayList<>();
        
        for (Payment payment : todayPayments) {
            String[] row = {
                payment.getPaymentTime().format(timeFormatter),
                payment.getOrderId(),
                String.format("$%.2f", payment.getAmount()),
                payment.getMethod().toString()
            };
            salesTableData.add(row);
        }
        
        view.updateSalesTable(salesTableData);
        
        // Update item statistics
        Map<String, Integer> itemStats = salesData.getItemSalesCount();
        view.updateItemStats(itemStats);
    }
}

