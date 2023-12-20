package main.java.gui;

import main.java.models.AVLTree;
import main.java.models.CV;
import main.java.utils.DocxUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Comparator;

/**
 * The main frame of the application.
 * 
 * @author <a href="https://github.com/Trustacean">Edward</a>
 */
public class MainFrame {

    private String[] sortBy = { "Id", "Name" };
    private String[] orders = { "In Order", "Pre Order", "Post Order" };
    private AVLTree tree;

    /**
     * Constructs the main frame, initializes components, data, and displays the
     * table.
     */
    public MainFrame() {
        initComponents();
        initializeData();
        showTable();
    }

    /**
     * Initializes the graphical components of the frame.
     */
    private void initComponents() {
        panel = new JPanel();
        panel.setLayout(null);

        searchField = new JTextField();
        searchField.setBounds(200, 440, 140, 20);
        panel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(360, 440, 100, 20);
        searchButton.addActionListener(e -> filterTable(searchField.getText()));
        panel.add(searchButton);

        sortByCombobox = new JComboBox<>(sortBy);
        sortByCombobox.setBounds(20, 440, 100, 20);
        sortByCombobox.addActionListener(e -> {
            updateTree(DocxUtils.extractCVsFromFile(DocxUtils.getFiles()));
            refreshTable(getOrderedCVs());
        });
        panel.add(sortByCombobox);

        orderCombobox = new JComboBox<>(orders);
        orderCombobox.setBounds(20, 480, 100, 20);
        orderCombobox.addActionListener(e -> {
            updateTree(DocxUtils.extractCVsFromFile(DocxUtils.getFiles()));
            refreshTable(getOrderedCVs());
        });
        panel.add(orderCombobox);

        table = new JTable();

        frame = new JFrame();
        frame.setTitle("CV Sorting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(panel);
    }

    /**
     * Initializes the data by extracting CVs from files and updating the AVL tree.
     */
    private void initializeData() {
        CV[] cvs = DocxUtils.extractCVsFromFile(DocxUtils.getFiles());
        tree = new AVLTree();
        updateTree(cvs);
    }

    /**
     * Updates the AVL tree based on the selected sorting criteria.
     *
     * @param cvs The array of CVs to be inserted into the tree.
     */
    private void updateTree(CV[] cvs) {
        tree = new AVLTree();
        String selectedSortBy = (String) sortByCombobox.getSelectedItem();

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

    /**
     * Sets up and displays the table in the frame.
     */
    private void showTable() {
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
    
    /**
     * Refreshes the table with the given CV array.
     *
     * @param results The array of CVs to be displayed in the table.
     */
    private void refreshTable(CV[] results) {
        tableModel.setRowCount(0);
        for (CV cv : results) {
            tableModel.addRow(new String[] { cv.getName(), cv.getId(), cv.getPosition1(), cv.getPosition2() });
        }
    }
    
    /**
     * Filters the table based on the search term and selected search criteria.
     *
     * @param searchTerm The term to search for in the table.
     */
    private void filterTable(String searchTerm) {
        if (searchTerm.isEmpty()) {
            refreshTable((CV[]) tree.inOrder());
        } else {
            CV[] filteredResults;
            
            String selectedSearchBy = (String) sortByCombobox.getSelectedItem();
            if ("Id".equals(selectedSearchBy)) {
                CV result = tree.searchById(searchTerm);
                filteredResults = (result != null) ? new CV[] { result } : new CV[0];
            } else if ("Name".equals(selectedSearchBy)) {
                CV result = tree.searchByName(searchTerm);
                filteredResults = (result != null) ? new CV[] { result } : new CV[0];
            } else {
                filteredResults = new CV[0];
            }
            
            refreshTable(filteredResults);
        }
    }
    
    /**
     * Gets the CVs from the tree based on the selected order.
     *
     * @return The array of CVs ordered based on the selected order.
     *         This method retrieves the CVs from the AVL tree based on the selected
     *         order (In Order, Pre Order, or Post Order).
     *         It returns an array of CVs representing the order in which they are
     *         sorted.
     */
    private CV[] getOrderedCVs() {
        String selectedOrder = (String) orderCombobox.getSelectedItem();
        if ("Pre Order".equals(selectedOrder)) {
            return (CV[]) tree.preOrder();
        } else if ("Post Order".equals(selectedOrder)) {
            return (CV[]) tree.postOrder();
        } else {
            // Default to in-order if the selected order is not recognized
            return (CV[]) tree.inOrder();
        }
    }

    private JFrame frame;
    private JTable table;
    private JPanel panel;
    private JComboBox<String> sortByCombobox;
    private JComboBox<String> orderCombobox;
    private JButton searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;
}
