package com.alexaegis.progtech.window.panels;


import com.alexaegis.progtech.database.Connector;
import com.alexaegis.progtech.logic.ResizeableElement;
import com.alexaegis.progtech.models.movies.Movie;

import javax.swing.*;

import java.sql.SQLException;

import static com.alexaegis.progtech.window.MainWindow.displayProperties;
import static java.lang.Thread.sleep;

public class ScrollPane extends JScrollPane implements ResizeableElement {

    private MovieGrid movieGrid;
    private LeaseGrid leaseGrid;

    public ScrollPane(Connector connector) throws SQLException {
        try {
            while(!connector.isConnected()) sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        movieGrid = new MovieGrid(connector);
        leaseGrid = new LeaseGrid(connector);
        setViewportView(movieGrid);
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        setVisible(true);
        revalidate();
        repaint();
    }

    public void setMovieGrid() {
        setViewportView(movieGrid);
        movieGrid.update();
        onResize();
    }

    public void setLeaseGrid() {
        setViewportView(leaseGrid);
        leaseGrid.update();
        onResize();
    }

    @Override
    public void onResize() {
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        revalidate();
        repaint();
    }
}
