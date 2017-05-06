package com.alexaegis.progtech.window.panels;

import com.alexaegis.progtech.database.Connector;
import com.alexaegis.progtech.database.MovieCache;
import com.alexaegis.progtech.database.MovieHandler;
import com.alexaegis.progtech.logic.ResizeableElement;
import com.alexaegis.progtech.logic.Updatable;
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

public class MovieGrid extends JTable implements Updatable, ResizeableElement {

    private MovieCache movieCache;

    public MovieGrid(Connector connector) throws SQLException {
        setLayout(new BorderLayout());
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        movieCache = new MovieCache(connector);
        DefaultTableModel defaultTableModel = new DefaultTableModel(movieCache.getData(), movieCache.getColumnNames());
        defaultTableModel.addColumn("Lease");
        defaultTableModel.addColumn("Remove");
        setModel(defaultTableModel);


        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e)
            {
                MovieHandler movieHandler = new MovieHandler(movieCache.getConnector());

                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf( e.getActionCommand() );
                long movieID = (long) table.getModel().getValueAt(modelRow, 0);
                System.out.println(movieID);
                for (Movie movie : movieCache.getMovies()) {
                    if(movie.getId() == movieID) {
                        try {
                            movieHandler.removeMovie(movie);
                            movieCache.update();
                            update();
                        } catch (IllegalMovieException e1) {
                            e1.printStackTrace();
                        }
                    }
                }


            }
        };
        ButtonColumn removeButtonColumn = new ButtonColumn(this, delete, 7);
        removeButtonColumn.setMnemonic(KeyEvent.VK_D);
        removeButtonColumn.setText("Remove");

        Action lease = new AbstractAction() {
            public void actionPerformed(ActionEvent e)
            {
                MovieHandler movieHandler = new MovieHandler(movieCache.getConnector());

                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf( e.getActionCommand() );
                long movieID = (long) table.getModel().getValueAt(modelRow, 0);
                System.out.println(movieID);
                for (Movie movie : movieCache.getMovies()) {
                    if(movie.getId() == movieID) {
                        new NewLeaseWindow(movie);
                    }
                }


            }
        };
        ButtonColumn leaseButtonColumn = new ButtonColumn(this, lease, 6);
        leaseButtonColumn.setMnemonic(KeyEvent.VK_D);
        leaseButtonColumn.setText("Lease");
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
       // ((DefaultTableModel) getModel()).setColumnCount(movieCache.getColumnNames().size() + 1);
        for (int i = 0; i < movieCache.getColumnNames().size() - 2; i++)
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

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column >= 6) return true;
        else return false;
    }
}