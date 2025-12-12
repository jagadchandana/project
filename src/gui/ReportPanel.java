package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import manager.ReportGenerator;
import java.util.*;

public class ReportPanel extends JPanel {
    private ReportGenerator reportGenerator;
    private JTextArea reportArea;

    public ReportPanel(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createReportArea(), BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Generate Reports"));

        JButton mostBorrowedButton = new JButton("Most Borrowed Books");
        JButton activeBorrowersButton = new JButton("Active Borrowers");
        JButton overdueButton = new JButton("Overdue Books");
        JButton categoryButton = new JButton("Books by Category");
        JButton membershipButton = new JButton("Users by Membership");
        JButton finesButton = new JButton("Total Fines");
        JButton statusButton = new JButton("Book Status Summary");

        mostBorrowedButton.addActionListener(e -> showMostBorrowedBooks());
        activeBorrowersButton.addActionListener(e -> showActiveBorrowers());
        overdueButton.addActionListener(e -> showOverdueBooks());
        categoryButton.addActionListener(e -> showBooksByCategory());
        membershipButton.addActionListener(e -> showUsersByMembership());
        finesButton.addActionListener(e -> showTotalFines());
        statusButton.addActionListener(e -> showBookStatusSummary());

        panel.add(mostBorrowedButton);
        panel.add(activeBorrowersButton);
        panel.add(overdueButton);
        panel.add(categoryButton);
        panel.add(membershipButton);
        panel.add(finesButton);
        panel.add(statusButton);

        return panel;
    }

    private JScrollPane createReportArea() {
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return new JScrollPane(reportArea);
    }

    private void showMostBorrowedBooks() {
        StringBuilder report = new StringBuilder();
        report.append("========== MOST BORROWED BOOKS ==========\n\n");
        
        Map<String, Integer> borrowedBooks = reportGenerator.getMostBorrowedBooks();
        if (borrowedBooks.isEmpty()) {
            report.append("No borrowing history available.\n");
        } else {
            int rank = 1;
            for (Map.Entry<String, Integer> entry : borrowedBooks.entrySet()) {
                report.append(String.format("%d. %s - %d times\n", rank++, entry.getKey(), entry.getValue()));
            }
        }
        
        reportArea.setText(report.toString());
    }

    private void showActiveBorrowers() {
        StringBuilder report = new StringBuilder();
        report.append("========== ACTIVE BORROWERS ==========\n\n");
        
        Map<String, Long> activeBorrowers = reportGenerator.getActiveBorrowers();
        if (activeBorrowers.isEmpty()) {
            report.append("No active borrowers.\n");
        } else {
            for (Map.Entry<String, Long> entry : activeBorrowers.entrySet()) {
                report.append(String.format("%s - %d book(s)\n", entry.getKey(), entry.getValue()));
            }
        }
        
        reportArea.setText(report.toString());
    }

    private void showOverdueBooks() {
        StringBuilder report = new StringBuilder();
        report.append("========== OVERDUE BOOKS ==========\n\n");
        
        java.util.List<Map<String, String>> overdueList = reportGenerator.getOverdueBooksReport();
        if (overdueList.isEmpty()) {
            report.append("No overdue books.\n");
        } else {
            for (Map<String, String> entry : overdueList) {
                report.append(String.format("User: %s\n", entry.get("User")));
                report.append(String.format("Book: %s\n", entry.get("Book")));
                report.append(String.format("Due Date: %s\n", entry.get("Due Date")));
                report.append(String.format("Overdue Days: %s\n\n", entry.get("Overdue Days")));
            }
        }
        
        reportArea.setText(report.toString());
    }

    private void showBooksByCategory() {
        StringBuilder report = new StringBuilder();
        report.append("========== BOOKS BY CATEGORY ==========\n\n");
        
        Map<String, Integer> categoryCount = reportGenerator.getBooksByCategory();
        if (categoryCount.isEmpty()) {
            report.append("No books available.\n");
        } else {
            for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
                report.append(String.format("%s: %d book(s)\n", entry.getKey(), entry.getValue()));
            }
        }
        
        reportArea.setText(report.toString());
    }

    private void showUsersByMembership() {
        StringBuilder report = new StringBuilder();
        report.append("========== USERS BY MEMBERSHIP TYPE ==========\n\n");
        
        Map<String, Long> membershipCount = reportGenerator.getUsersByMembershipType();
        for (Map.Entry<String, Long> entry : membershipCount.entrySet()) {
            report.append(String.format("%s: %d user(s)\n", entry.getKey(), entry.getValue()));
        }
        
        reportArea.setText(report.toString());
    }

    private void showTotalFines() {
        StringBuilder report = new StringBuilder();
        report.append("========== TOTAL FINES COLLECTED ==========\n\n");
        
        double totalFines = reportGenerator.getTotalFinesCollected();
        report.append(String.format("Total Fines: LKR %.2f\n", totalFines));
        
        reportArea.setText(report.toString());
    }

    private void showBookStatusSummary() {
        StringBuilder report = new StringBuilder();
        report.append("========== BOOK STATUS SUMMARY ==========\n\n");
        
        Map<String, Integer> statusCount = reportGenerator.getAvailabilityStatusCount();
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            report.append(String.format("%s: %d book(s)\n", entry.getKey(), entry.getValue()));
        }
        
        reportArea.setText(report.toString());
    }
}
