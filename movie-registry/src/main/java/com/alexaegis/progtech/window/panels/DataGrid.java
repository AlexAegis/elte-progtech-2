package com.alexaegis.progtech.window.panels;

import com.alexaegis.progtech.database.Connector;
import com.alexaegis.progtech.database.MovieCache;
import com.alexaegis.progtech.logic.ResizeableElement;
import com.alexaegis.progtech.logic.Updatable;

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

    private MovieCache movieCache;

    public DataGrid(Connector connector) throws SQLException {
        setLayout(new BorderLayout());
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        movieCache = new MovieCache(connector);
        setModel(new DefaultTableModel(movieCache.getData(), movieCache.getColumnNames()));

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
        movieCache.update();
        ((DefaultTableModel) getModel()).setRowCount(movieCache.getData().size());
        ((DefaultTableModel) getModel()).setColumnCount(movieCache.getColumnNames().size());
        for (int i = 0; i < movieCache.getColumnNames().size(); i++)
            for (int j = 0; j < movieCache.getData().size(); j++)
                getModel().setValueAt(movieCache.getData().get(j).get(i), j, i);
        ((AbstractTableModel) getModel()).fireTableDataChanged();
        revalidate();
        repaint();
    }

    @Override
    public void onResize() {
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
    }
}