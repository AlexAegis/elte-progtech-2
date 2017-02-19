package com.alexaegis.progtech.window.panels;

import com.github.alexaegis.swing.Updatable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import static com.alexaegis.progtech.Main.connector;

public class DataGrid extends JTable implements Updatable {

    private Logger logger = Logger.getLogger(getClass().getName());
    private DefaultTableModel tableModel;
    private java.util.List<String> headers = new ArrayList<>();
    private String query = "SELECT * FROM Example";
    Vector<Vector<Object>> data = new Vector<>();

    public DataGrid() throws SQLException {
        setLayout(new BorderLayout());
        setBounds(0,0, 200, 200);
        tableModel = buildTableModel(connector.query(query));
        setModel(tableModel);
        setVisible(true);
        revalidate();
        repaint();
    }

    public void update() {
        try {
            updateTableModel(tableModel, connector.query(query));
            revalidate();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        // data of the table
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(resultSet.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }

    private void updateTableModel(DefaultTableModel tableModel, ResultSet resultSet) throws SQLException { //heavy rework needed
        int columnCount= resultSet.getMetaData().getColumnCount();
        tableModel.setColumnCount(columnCount);
        resultSet.last();
        int rowCount = resultSet.getRow();
        int rowIndex = 0;
        resultSet.beforeFirst();
        if(tableModel.getRowCount() != rowCount) {
            tableModel.setRowCount(rowCount);
        }

        while (resultSet.next()) {
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                Object current = resultSet.getObject(columnIndex);
                if(!data.get(rowIndex).get(columnIndex - 1).equals(current)) {
                    data.get(rowIndex).set(columnIndex - 1, current);
                    tableModel.setValueAt(current, rowIndex, columnIndex - 1);
                }
                resultSet.getObject(columnIndex);
            }
            rowIndex++;
        }
    }
}
