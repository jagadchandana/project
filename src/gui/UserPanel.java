package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import manager.LibraryManager;
import model.User;
import model.MembershipType;

public class UserPanel extends JPanel {
    private LibraryManager libraryManager;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField userIdField, nameField, emailField, contactField;
    private JComboBox<MembershipType> membershipCombo;

    public UserPanel(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        
        refreshTable();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("User Management"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userIdField = new JTextField(15);
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        contactField = new JTextField(15);
        membershipCombo = new JComboBox<>(MembershipType.values());

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 3;
        panel.add(contactField, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Membership:"), gbc);
        gbc.gridx = 3;
        panel.add(membershipCombo, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add User");
        JButton updateButton = new JButton("Update User");
        JButton deleteButton = new JButton("Delete User");
        JButton clearButton = new JButton("Clear");

        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("User List"));

        String[] columns = {"User ID", "Name", "Email", "Contact", "Membership", "Active Books"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                loadUserToForm();
            }
        });

        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addUser() {
        try {
            if (userIdField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "User ID, Name, and Email are required!");
                return;
            }

            User user = new User(
                userIdField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim(),
                contactField.getText().trim(),
                (MembershipType) membershipCombo.getSelectedItem()
            );

            libraryManager.addUser(user);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "User added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage());
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update!");
            return;
        }

        try {
            String userId = userIdField.getText().trim();
            User existingUser = libraryManager.getUser(userId);
            if (existingUser == null) {
                JOptionPane.showMessageDialog(this, "User not found!");
                return;
            }

            existingUser.setName(nameField.getText().trim());
            existingUser.setEmail(emailField.getText().trim());
            existingUser.setContactNumber(contactField.getText().trim());
            existingUser.setMembershipType((MembershipType) membershipCombo.getSelectedItem());

            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "User updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage());
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete!");
            return;
        }

        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", 
                                                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            libraryManager.removeUser(userId);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "User deleted successfully!");
        }
    }

    private void loadUserToForm() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            String userId = (String) tableModel.getValueAt(selectedRow, 0);
            User user = libraryManager.getUser(userId);
            if (user != null) {
                userIdField.setText(user.getUserId());
                nameField.setText(user.getName());
                emailField.setText(user.getEmail());
                contactField.setText(user.getContactNumber());
                membershipCombo.setSelectedItem(user.getMembershipType());
            }
        }
    }

    private void clearFields() {
        userIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        contactField.setText("");
        membershipCombo.setSelectedIndex(0);
        userTable.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (User user : libraryManager.getAllUsers()) {
            tableModel.addRow(new Object[]{
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getContactNumber(),
                user.getMembershipType(),
                user.getCurrentActiveBorrowCount()
            });
        }
    }
}
