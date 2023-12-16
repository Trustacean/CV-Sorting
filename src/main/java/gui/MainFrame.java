package main.java.gui;

import main.java.models.AVLTree;
import main.java.models.CV;
import main.java.utils.DocxUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;

public class MainFrame {
    private JFrame frame;
    private JTable table;
    private JPanel panel;
    private JComboBox<String> comboBox;
    private JButton searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    private String[] comboBoxItems = {"Id", "Name"};
    private int selectedRow = -1;
    private AVLTree tree;
    private ArrayList<CV> result;

    public MainFrame() {
        initComponents();
        initializeData();
        showTable();
    }

    private void initializeData() {
        // Extract CVs from files and create an AVL tree based on the selected sorting option
        ArrayList<CV> cvs = DocxUtils.extractCVsFromFile(DocxUtils.getFiles());
        tree = new AVLTree();
        updateTree(cvs);
    }

    private void updateTree(ArrayList<CV> cvs) {
        tree = new AVLTree();
        // Update the AVL tree based on the selected sorting option
        String selectedSortBy = (String) comboBox.getSelectedItem();

        if ("Id".equals(selectedSortBy)) {
            for (CV cv : cvs) {
                tree.insert(cv, Comparator.comparing(CV::getId));
            }
        } else if ("Name".equals(selectedSortBy)) {
            for (CV cv : cvs) {
                tree.insert(cv, Comparator.comparing(CV::getName));
            }
        }
    }

    private void initComponents() {
        // Initialize GUI components
        panel = new JPanel();
        panel.setLayout(null);

        searchField = new JTextField();
        searchField.setBounds(200, 440, 140, 20);
        panel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(360, 440, 100, 20);
        searchButton.addActionListener(e -> filterTable(searchField.getText()));
        panel.add(searchButton);

        comboBox = new JComboBox<>(comboBoxItems);
        comboBox.setBounds(20, 440, 100, 20);
        comboBox.addActionListener(e -> {
            // When the sortBy JComboBox selection changes, update the tree and refresh the table
            updateTree(DocxUtils.extractCVsFromFile(DocxUtils.getFiles()));
            refreshTable();
        });
        panel.add(comboBox);

        table = new JTable();

        frame = new JFrame();
        frame.setTitle("CV Sorting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(panel);
    }

    private void showTable() {
        // Display the table with the initial data
        String[] columnNames = {"Name", "Id", "First Choice", "Second Choice"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(15, 20, 460, 400);
        panel.add(scrollPane);
    }

    private void refreshTable() {
        // Refresh the table with data from the AVL tree
        tableModel.setRowCount(0);
        result = tree.inOrder();
        for (CV cv : result) {
            tableModel.addRow(new String[]{cv.getName(), cv.getId(), cv.getPosition1(), cv.getPosition2()});
        }
    }

    private void filterTable(String searchTerm) {
        // Filter the table based on the search term
        tableModel.setRowCount(0);

        if (result == null || result.isEmpty()) {
            return; // No data to filter
        }

        searchTerm = searchTerm.toLowerCase(); // Convert search term to lowercase for case-insensitive search

        for (CV cv : result) {
            // Check if the ID or Name contains the search term
            if (cv.getId().contains(searchTerm) || cv.getName().toLowerCase().contains(searchTerm)) {
                tableModel.addRow(new String[]{cv.getName(), cv.getId(), cv.getPosition1(), cv.getPosition2()});
            }
        }
    }
}
