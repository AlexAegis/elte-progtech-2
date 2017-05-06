package com.alexaegis.progtech.window.panels;

import com.alexaegis.progtech.database.Cache;
import com.alexaegis.progtech.database.Connector;
import com.alexaegis.progtech.database.MovieCache;
import com.alexaegis.progtech.database.MovieHandler;
import com.alexaegis.progtech.logic.ResizeableElement;
import com.alexaegis.progtech.logic.Updatable;
import com.alexaegis.progtech.misc.IllegalLeaseException;
import com.alexaegis.progtech.misc.IllegalMovieException;
import com.alexaegis.progtech.models.movies.Movie;
import com.alexaegis.progtech.window.NewLeaseWindow;
import com.alexaegis.progtech.window.elements.ButtonColumn;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.alexaegis.progtech.window.MainWindow.displayProperties;

public class LeaseGrid extends JTable implements Updatable, ResizeableElement {

    private Cache cache;

    public LeaseGrid(Connector connector) throws SQLException {
        setLayout(new BorderLayout());
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        cache = new Cache(connector);
        cache.setQuery("SELECT * FROM APP.LEASES");
        cache.update();
        DefaultTableModel defaultTableModel = new DefaultTableModel(cache.getData(), cache.getColumnNames());
        defaultTableModel.addColumn("Remove");
        setModel(defaultTableModel);


        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e)
            {
                MovieHandler movieHandler = new MovieHandler(cache.getConnector());

                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf( e.getActionCommand() );
                long movieID = (long) table.getModel().getValueAt(modelRow, 1);
                try {
                    movieHandler.removeLease(movieID);
                } catch (IllegalLeaseException e1) {
                    e1.printStackTrace();
                }

            }
        };
        ButtonColumn removeButtonColumn = new ButtonColumn(this, delete, 4);
        removeButtonColumn.setMnemonic(KeyEvent.VK_D);
        removeButtonColumn.setText("Remove");

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
        // ((DefaultTableModel) getModel()).setColumnCount(movieCache.getColumnNames().size() + 1);
        for (int i = 0; i < cache.getColumnNames().size() - 1; i++)
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

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column >= 4) return true;
        else return false;
    }
}