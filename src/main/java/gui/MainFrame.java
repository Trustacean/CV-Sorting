package main.java.gui;

import main.java.models.AVLTree;
import main.java.models.CV;
import main.java.utils.DocxUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;

public class MainFrame {
    
    private String[] comboBoxItems = { "Id", "Name" };
    private AVLTree tree;
    
    public MainFrame() {
        initComponents();
        initializeData();
        showTable();
    }
    
    private void initializeData() {
        // Extract CVs from files and create an AVL tree based on the selected sorting
        // option
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
            // When the sortBy JComboBox selection changes, update the tree and refresh the
            // table
            updateTree(DocxUtils.extractCVsFromFile(DocxUtils.getFiles()));
            refreshTable((CV[]) tree.inOrder());
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
        String[] columnNames = { "Name", "Id", "First Choice", "Second Choice" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        refreshTable((CV[]) tree.inOrder());
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(15, 20, 460, 400);
        panel.add(scrollPane);
    }
    
    private void refreshTable(CV[] results) {
        // Refresh the table with data from the AVL tree
        tableModel.setRowCount(0);
        for (CV cv : results) {
            tableModel.addRow(new String[] { cv.getName(), cv.getId(), cv.getPosition1(), cv.getPosition2() });
        }
    }
    
    private void filterTable(String searchTerm) {
        if (searchTerm.isEmpty()) {
            // If the search term is empty, refresh the table with all data
            refreshTable((CV[]) tree.inOrder());
        } else {
            CV[] filteredResults;
    
            // Depending on the search criteria, populate the filteredResults array
            String selectedSearchBy = (String) comboBox.getSelectedItem();
            if ("Id".equals(selectedSearchBy)) {
                CV result = tree.searchById(searchTerm);
                filteredResults = (result != null) ? new CV[]{result} : new CV[0];
            } else if ("Name".equals(selectedSearchBy)) {
                CV result = tree.searchByName(searchTerm);
                filteredResults = (result != null) ? new CV[]{result} : new CV[0];
            } else {
                // Handle other search criteria if needed
                filteredResults = new CV[0];
            }
    
            // Refresh the table with the filtered results
            refreshTable(filteredResults);
        }
    }
    
    private JFrame frame;
    private JTable table;
    private JPanel panel;
    private JComboBox<String> comboBox;
    private JButton searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;
}
