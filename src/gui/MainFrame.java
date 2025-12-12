package gui;

import javax.swing.*;
import java.awt.*;
import manager.LibraryManager;
import manager.ReportGenerator;
import command.CommandInvoker;

public class MainFrame extends JFrame {
    private LibraryManager libraryManager;
    private ReportGenerator reportGenerator;
    private CommandInvoker commandInvoker;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        this.libraryManager = new LibraryManager();
        this.reportGenerator = new ReportGenerator(libraryManager);
        this.commandInvoker = new CommandInvoker();
        
        initializeUI();
        initializeSampleData();
    }

    private void initializeUI() {
        setTitle("Smart Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Books", new BookPanel(libraryManager));
        tabbedPane.addTab("Users", new UserPanel(libraryManager));
        tabbedPane.addTab("Borrow/Return", new BorrowReturnPanel(libraryManager, commandInvoker));
        tabbedPane.addTab("Reservations", new ReservationPanel(libraryManager, commandInvoker));
        tabbedPane.addTab("Reports", new ReportPanel(reportGenerator));
        tabbedPane.addTab("Notifications", new NotificationPanel(libraryManager));

        add(tabbedPane);
    }

    private void initializeSampleData() {
        SampleDataInitializer.initializeSampleData(libraryManager);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
