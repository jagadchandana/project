package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import manager.LibraryManager;
import model.*;
import command.*;

public class BorrowReturnPanel extends JPanel {
    private LibraryManager libraryManager;
    private CommandInvoker commandInvoker;
    private JComboBox<String> userCombo, bookCombo;
    private JTable recordTable;
    private DefaultTableModel tableModel;

    public BorrowReturnPanel(LibraryManager libraryManager, CommandInvoker commandInvoker) {
        this.libraryManager = libraryManager;
        this.commandInvoker = commandInvoker;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createActionPanel(), BorderLayout.NORTH);
        add(createRecordPanel(), BorderLayout.CENTER);
        
        refreshData();
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Borrow/Return Actions"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userCombo = new JComboBox<>();
        bookCombo = new JComboBox<>();

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select User:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(userCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Select Book:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(bookCombo, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton borrowButton = new JButton("Borrow Book");
        JButton returnButton = new JButton("Return Book");
        JButton undoButton = new JButton("Undo Last Action");
        JButton refreshButton = new JButton("Refresh");

        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        undoButton.addActionListener(e -> undoLastAction());
        refreshButton.addActionListener(e -> refreshData());

        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(refreshButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createRecordPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Active Borrow Records"));

        String[] columns = {"Record ID", "User", "Book", "Borrow Date", "Due Date", "Status", "Overdue Days"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recordTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(recordTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void borrowBook() {
        try {
            String selectedUser = (String) userCombo.getSelectedItem();
            String selectedBook = (String) bookCombo.getSelectedItem();

            if (selectedUser == null || selectedBook == null) {
                JOptionPane.showMessageDialog(this, "Please select both user and book!");
                return;
            }

            String userId = selectedUser.split(" - ")[0];
            String bookId = selectedBook.split(" - ")[0];

            User user = libraryManager.getUser(userId);
            Book book = libraryManager.getBook(bookId);

            if (user == null || book == null) {
                JOptionPane.showMessageDialog(this, "Invalid user or book selection!");
                return;
            }

            Command borrowCommand = new BorrowCommand(libraryManager, user, book);
            commandInvoker.executeCommand(borrowCommand);

            refreshData();
            JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error borrowing book: " + e.getMessage());
        }
    }

    private void returnBook() {
        try {
            String selectedUser = (String) userCombo.getSelectedItem();
            String selectedBook = (String) bookCombo.getSelectedItem();

            if (selectedUser == null || selectedBook == null) {
                JOptionPane.showMessageDialog(this, "Please select both user and book!");
                return;
            }

            String userId = selectedUser.split(" - ")[0];
            String bookId = selectedBook.split(" - ")[0];

            User user = libraryManager.getUser(userId);
            Book book = libraryManager.getBook(bookId);

            if (user == null || book == null) {
                JOptionPane.showMessageDialog(this, "Invalid user or book selection!");
                return;
            }

            Command returnCommand = new ReturnCommand(libraryManager, user, book);
            commandInvoker.executeCommand(returnCommand);

            refreshData();
            JOptionPane.showMessageDialog(this, "Book returned successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error returning book: " + e.getMessage());
        }
    }

    private void undoLastAction() {
        try {
            commandInvoker.undoLastCommand();
            refreshData();
            JOptionPane.showMessageDialog(this, "Last action undone successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error undoing action: " + e.getMessage());
        }
    }

    private void refreshData() {
        userCombo.removeAllItems();
        for (User user : libraryManager.getAllUsers()) {
            userCombo.addItem(user.getUserId() + " - " + user.getName());
        }

        bookCombo.removeAllItems();
        for (Book book : libraryManager.getAllBooks()) {
            bookCombo.addItem(book.getBookId() + " - " + book.getTitle());
        }

        tableModel.setRowCount(0);
        for (BorrowRecord record : libraryManager.getAllBorrowRecords()) {
            if (record.getReturnDate() == null) {
                User user = libraryManager.getUser(record.getUserId());
                Book book = libraryManager.getBook(record.getBookId());
                
                String status = record.isOverdue() ? "OVERDUE" : "Active";
                long overdueDays = record.isOverdue() ? record.getOverdueDays() : 0;

                tableModel.addRow(new Object[]{
                    record.getRecordId(),
                    user != null ? user.getName() : record.getUserId(),
                    book != null ? book.getTitle() : record.getBookId(),
                    record.getBorrowDate(),
                    record.getDueDate(),
                    status,
                    overdueDays
                });
            }
        }
    }
}
