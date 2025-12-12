package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import manager.LibraryManager;
import model.Book;
import builder.BookBuilder;
import decorator.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BookPanel extends JPanel {
    private LibraryManager libraryManager;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField bookIdField, titleField, authorField, categoryField, isbnField;
    private JTextField editionField, reviewsField, tagsField;
    private JCheckBox featuredCheck, recommendedCheck, specialEditionCheck;

    public BookPanel(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        
        refreshTable();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Book Management"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        bookIdField = new JTextField(15);
        titleField = new JTextField(15);
        authorField = new JTextField(15);
        categoryField = new JTextField(15);
        isbnField = new JTextField(15);
        editionField = new JTextField(15);
        reviewsField = new JTextField(15);
        tagsField = new JTextField(15);
        
        featuredCheck = new JCheckBox("Featured");
        recommendedCheck = new JCheckBox("Recommended");
        specialEditionCheck = new JCheckBox("Special Edition");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        panel.add(bookIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        panel.add(authorField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 3;
        panel.add(categoryField, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 3;
        panel.add(isbnField, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(new JLabel("Edition:"), gbc);
        gbc.gridx = 3;
        panel.add(editionField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Reviews:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(reviewsField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panel.add(new JLabel("Tags (comma-separated):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(tagsField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panel.add(featuredCheck, gbc);
        gbc.gridx = 1;
        panel.add(recommendedCheck, gbc);
        gbc.gridx = 2;
        panel.add(specialEditionCheck, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Book");
        JButton updateButton = new JButton("Update Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton clearButton = new JButton("Clear");

        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Book List"));

        String[] columns = {"Book ID", "Title", "Author", "Category", "ISBN", "Status", "Edition"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                loadBookToForm();
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addBook() {
        try {
            if (bookIdField.getText().trim().isEmpty() || titleField.getText().trim().isEmpty() || 
                authorField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Book ID, Title, and Author are required!");
                return;
            }

            BookBuilder builder = new BookBuilder(
                bookIdField.getText().trim(),
                titleField.getText().trim(),
                authorField.getText().trim()
            );

            if (!categoryField.getText().trim().isEmpty()) {
                builder.setCategory(categoryField.getText().trim());
            }
            if (!isbnField.getText().trim().isEmpty()) {
                builder.setIsbn(isbnField.getText().trim());
            }
            if (!editionField.getText().trim().isEmpty()) {
                builder.setEdition(editionField.getText().trim());
            }
            if (!reviewsField.getText().trim().isEmpty()) {
                builder.setReviews(reviewsField.getText().trim());
            }
            if (!tagsField.getText().trim().isEmpty()) {
                String[] tags = tagsField.getText().split(",");
                builder.setTags(Arrays.asList(tags));
            }

            Book book = builder.build();

            if (featuredCheck.isSelected()) {
                book = new FeaturedBookDecorator(book);
            }
            if (recommendedCheck.isSelected()) {
                book = new RecommendedBookDecorator(book);
            }
            if (specialEditionCheck.isSelected()) {
                book = new SpecialEditionDecorator(book);
            }

            libraryManager.addBook(book);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Book added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + e.getMessage());
        }
    }

    private void updateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update!");
            return;
        }

        try {
            String bookId = bookIdField.getText().trim();
            Book existingBook = libraryManager.getBook(bookId);
            if (existingBook == null) {
                JOptionPane.showMessageDialog(this, "Book not found!");
                return;
            }

            BookBuilder builder = new BookBuilder(bookId, titleField.getText().trim(), authorField.getText().trim());
            builder.setCategory(categoryField.getText().trim())
                   .setIsbn(isbnField.getText().trim())
                   .setEdition(editionField.getText().trim())
                   .setReviews(reviewsField.getText().trim());

            if (!tagsField.getText().trim().isEmpty()) {
                builder.setTags(Arrays.asList(tagsField.getText().split(",")));
            }

            Book book = builder.build();
            book.setState(existingBook.getState());

            libraryManager.updateBook(book);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Book updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + e.getMessage());
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete!");
            return;
        }

        String bookId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", 
                                                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            libraryManager.removeBook(bookId);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Book deleted successfully!");
        }
    }

    private void loadBookToForm() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            String bookId = (String) tableModel.getValueAt(selectedRow, 0);
            Book book = libraryManager.getBook(bookId);
            if (book != null) {
                bookIdField.setText(book.getBookId());
                titleField.setText(book.getTitle());
                authorField.setText(book.getAuthor());
                categoryField.setText(book.getCategory());
                isbnField.setText(book.getIsbn());
                editionField.setText(book.getEdition() != null ? book.getEdition() : "");
                reviewsField.setText(book.getReviews() != null ? book.getReviews() : "");
                tagsField.setText(book.getTags() != null ? String.join(",", book.getTags()) : "");
            }
        }
    }

    private void clearFields() {
        bookIdField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        isbnField.setText("");
        editionField.setText("");
        reviewsField.setText("");
        tagsField.setText("");
        featuredCheck.setSelected(false);
        recommendedCheck.setSelected(false);
        specialEditionCheck.setSelected(false);
        bookTable.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Book book : libraryManager.getAllBooks()) {
            tableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getIsbn(),
                book.getAvailabilityStatus(),
                book.getEdition() != null ? book.getEdition() : ""
            });
        }
    }
}
