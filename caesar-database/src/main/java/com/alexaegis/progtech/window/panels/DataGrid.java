package com.alexaegis.progtech.window.panels;

import com.alexaegis.progtech.database.Cache;
import com.alexaegis.progtech.database.Connector;
import com.github.alexaegis.swing.ResizeableElement;
import com.github.alexaegis.swing.Updatable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.alexaegis.progtech.window.MainWindow.displayProperties;

public class DataGrid extends JTable implements Updatable, ResizeableElement {

    private Cache cache;

    public DataGrid(Connector connector, String tableName) throws SQLException {
        setLayout(new BorderLayout());
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        cache = new Cache(connector, tableName);
        setModel(new DefaultTableModel(cache.getData(), cache.getColumnNames()));

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(getModel());
        setRowSorter(sorter);

        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        for (int i = 0; i < getModel().getColumnCount(); i++) {
            sortKeys.add(new RowSorter.SortKey(i, SortOrder.ASCENDING));
        }
        sorter.setSortKeys(sortKeys);

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

    @Override
    public void onResize() {
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
    }
}