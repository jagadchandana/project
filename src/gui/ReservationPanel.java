package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import manager.LibraryManager;
import model.*;
import command.*;

public class ReservationPanel extends JPanel {
    private LibraryManager libraryManager;
    private CommandInvoker commandInvoker;
    private JComboBox<String> userCombo, bookCombo;
    private JTable reservationTable;
    private DefaultTableModel tableModel;

    public ReservationPanel(LibraryManager libraryManager, CommandInvoker commandInvoker) {
        this.libraryManager = libraryManager;
        this.commandInvoker = commandInvoker;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createActionPanel(), BorderLayout.NORTH);
        add(createReservationListPanel(), BorderLayout.CENTER);
        
        refreshData();
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Reservation Actions"));
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
        JButton reserveButton = new JButton("Reserve Book");
        JButton cancelButton = new JButton("Cancel Reservation");
        JButton refreshButton = new JButton("Refresh");

        reserveButton.addActionListener(e -> reserveBook());
        cancelButton.addActionListener(e -> cancelReservation());
        refreshButton.addActionListener(e -> refreshData());

        buttonPanel.add(reserveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createReservationListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Active Reservations"));

        String[] columns = {"Reservation ID", "User", "Book", "Reservation Date", "Notified"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(reservationTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void reserveBook() {
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

            Command reserveCommand = new ReserveCommand(libraryManager, user, book);
            commandInvoker.executeCommand(reserveCommand);

            refreshData();
            JOptionPane.showMessageDialog(this, "Book reserved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reserving book: " + e.getMessage());
        }
    }

    private void cancelReservation() {
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

            Command cancelCommand = new CancelReservationCommand(libraryManager, user, book);
            commandInvoker.executeCommand(cancelCommand);

            refreshData();
            JOptionPane.showMessageDialog(this, "Reservation cancelled successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cancelling reservation: " + e.getMessage());
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
        for (Reservation reservation : libraryManager.getAllReservations()) {
            User user = libraryManager.getUser(reservation.getUserId());
            Book book = libraryManager.getBook(reservation.getBookId());

            tableModel.addRow(new Object[]{
                reservation.getReservationId(),
                user != null ? user.getName() : reservation.getUserId(),
                book != null ? book.getTitle() : reservation.getBookId(),
                reservation.getReservationDate(),
                reservation.isNotified() ? "Yes" : "No"
            });
        }
    }
}
