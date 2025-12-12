package gui;

import javax.swing.*;
import java.awt.*;
import manager.LibraryManager;
import model.User;
import java.util.List;

public class NotificationPanel extends JPanel {
    private LibraryManager libraryManager;
    private JComboBox<String> userCombo;
    private JTextArea notificationArea;
    private JButton checkOverdueButton;
    private JButton checkDueSoonButton;

    public NotificationPanel(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createControlPanel(), BorderLayout.NORTH);
        add(createNotificationArea(), BorderLayout.CENTER);
        
        refreshUserList();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Notification Controls"));

        panel.add(new JLabel("Select User:"));
        userCombo = new JComboBox<>();
        userCombo.addActionListener(e -> displayUserNotifications());
        panel.add(userCombo);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            refreshUserList();
            displayUserNotifications();
        });
        panel.add(refreshButton);

        checkOverdueButton = new JButton("Check Overdue Books");
        checkOverdueButton.addActionListener(e -> checkOverdueBooks());
        panel.add(checkOverdueButton);

        checkDueSoonButton = new JButton("Check Due Soon");
        checkDueSoonButton.addActionListener(e -> checkDueSoonBooks());
        panel.add(checkDueSoonButton);

        JButton clearButton = new JButton("Clear User Notifications");
        clearButton.addActionListener(e -> clearUserNotifications());
        panel.add(clearButton);

        return panel;
    }

    private JScrollPane createNotificationArea() {
        notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        notificationArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        notificationArea.setLineWrap(true);
        notificationArea.setWrapStyleWord(true);
        return new JScrollPane(notificationArea);
    }

    private void refreshUserList() {
        userCombo.removeAllItems();
        for (User user : libraryManager.getAllUsers()) {
            userCombo.addItem(user.getUserId() + " - " + user.getName());
        }
    }

    private void displayUserNotifications() {
        String selectedUser = (String) userCombo.getSelectedItem();
        if (selectedUser == null) {
            notificationArea.setText("No user selected.");
            return;
        }

        String userId = selectedUser.split(" - ")[0];
        User user = libraryManager.getUser(userId);

        if (user == null) {
            notificationArea.setText("User not found.");
            return;
        }

        StringBuilder notifications = new StringBuilder();
        notifications.append("========== NOTIFICATIONS FOR ").append(user.getName()).append(" ==========\n\n");

        List<String> userNotifications = user.getNotifications();
        if (userNotifications.isEmpty()) {
            notifications.append("No notifications.\n");
        } else {
            for (int i = 0; i < userNotifications.size(); i++) {
                notifications.append((i + 1)).append(". ").append(userNotifications.get(i)).append("\n");
            }
        }

        notificationArea.setText(notifications.toString());
    }

    private void checkOverdueBooks() {
        libraryManager.checkAndNotifyOverdue();
        JOptionPane.showMessageDialog(this, "Overdue notifications sent to affected users!");
        displayUserNotifications();
    }

    private void checkDueSoonBooks() {
        libraryManager.checkAndNotifyDueSoon();
        JOptionPane.showMessageDialog(this, "Due soon notifications sent to affected users!");
        displayUserNotifications();
    }

    private void clearUserNotifications() {
        String selectedUser = (String) userCombo.getSelectedItem();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "No user selected!");
            return;
        }

        String userId = selectedUser.split(" - ")[0];
        User user = libraryManager.getUser(userId);

        if (user != null) {
            user.getNotifications().clear();
            displayUserNotifications();
            JOptionPane.showMessageDialog(this, "Notifications cleared for " + user.getName());
        }
    }
}
