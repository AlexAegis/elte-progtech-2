package com.alexaegis.progtech.window.panels;

import com.alexaegis.progtech.database.Cache;
import com.github.alexaegis.swing.Updatable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;


public class DataGrid extends JTable implements Updatable {

    private Cache cache;

    public DataGrid(Connection connection, String tableName) throws SQLException {
        setLayout(new BorderLayout());
        setBounds(0,0, 200, 200);
        cache = new Cache(connection, tableName);
        setModel(new DefaultTableModel(cache.getData(), cache.getColumnNames()));
        setVisible(true);
        revalidate();
        repaint();
    }

    @Override
    public void update() {
        cache.update();
        ((DefaultTableModel) getModel()).setRowCount(cache.getData().size());
        ((DefaultTableModel) getModel()).setColumnCount(cache.getColumnNames().size());
        for (int i = 0; i < cache.getColumnNames().size(); i++)
            for (int j = 0; j < cache.getData().size(); j++)
                getModel().setValueAt(cache.getData().get(j).get(i), j, i);
        ((AbstractTableModel) getModel()).fireTableDataChanged();
        revalidate();
        repaint();
    }
}