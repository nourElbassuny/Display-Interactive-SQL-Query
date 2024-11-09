package org.example;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;

public class DisplayQueryResults extends JFrame {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3305;
    private static final String DB_NAME = "northwind";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Nour0112621@@";
    private static final String DATABASE_URL = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DB_NAME);
    static final String DEFAULT_QUERY = "SELECT * from northwind.product";
    private ResultSetTableModel tableModel;
    private JTextArea queryArea;

    public DisplayQueryResults() {
        super("Display Query Results");
        try {
            tableModel = new ResultSetTableModel(DATABASE_URL,
                    USERNAME, PASSWORD, DEFAULT_QUERY);
            queryArea = new JTextArea(DEFAULT_QUERY, 3, 100);
            queryArea.setWrapStyleWord(true);
            queryArea.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(queryArea,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            JButton submitButton = new JButton("Submit Query");

            Box boxNorth = Box.createHorizontalBox();
            boxNorth.add(scrollPane);
            boxNorth.add(submitButton);

            JTable resultsTable = new JTable(tableModel);

            JLabel filterLabel = new JLabel("Filter:");
            final JTextField filterText = new JTextField();
            JButton filterButton = new JButton("Apply Filter");
            Box boxSouth = Box.createHorizontalBox();
            boxSouth.add(filterLabel);
            boxSouth.add(filterText);
            boxSouth.add(filterButton);

            add(boxNorth, BorderLayout.NORTH);
            add(new JScrollPane(resultsTable), BorderLayout.CENTER);
            add(boxSouth, BorderLayout.SOUTH);
            submitButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            try{
                                tableModel.setQuery(queryArea.getText());
                            }catch (SQLException sqlException){
                                JOptionPane.showMessageDialog(null,
                                        sqlException.getMessage(),
                                        "Database error",
                                        JOptionPane.ERROR_MESSAGE);
                                try {
                                    tableModel.setQuery(DEFAULT_QUERY);
                                    queryArea.setText(DEFAULT_QUERY);
                                }catch (SQLException sqlException2){
                                    JOptionPane.showMessageDialog(null,
                                            sqlException2.getMessage(),"Database error",
                                            JOptionPane.ERROR_MESSAGE);
                                    tableModel.disconnectFromDatabase();
                                    System.exit(1);
                                }
                            }
                        }
                    }
            );
            final TableRowSorter<TableModel> sorter=
                    new TableRowSorter< TableModel>(tableModel );
            resultsTable.setRowSorter(sorter);
            setSize(500,250);
            setVisible(true);

            filterButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String text = filterText.getText();
                            if (text.length()==0)
                                sorter.setRowFilter(null);
                            else {
                                try {
                                    sorter.setRowFilter(
                                            RowFilter.regexFilter(text)
                                    );
                                }catch (PatternSyntaxException pse){
                                    JOptionPane.showMessageDialog(null,
                                            "Bad regex pattern","Bad regex patten",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
            );
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog( null,sqlException.getMessage(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
            tableModel.disconnectFromDatabase();
            System.exit(1);
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                tableModel.disconnectFromDatabase();
                System.exit(0);
            }
        });
    }
}
