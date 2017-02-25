package com.alexaegis.progtech.window.panels;


import com.alexaegis.progtech.database.Connector;
import com.github.alexaegis.swing.ResizeableElement;

import javax.swing.*;

import java.sql.SQLException;

import static com.alexaegis.progtech.window.MainWindow.displayProperties;
import static java.lang.Thread.sleep;

public class ScrollPane extends JScrollPane implements ResizeableElement {

    public ScrollPane(Connector connector) throws SQLException {
        try {
            while(!connector.isConnected()) sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataGrid dataGrid = new DataGrid(connector);
        setViewportView(dataGrid);
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        setVisible(true);
        revalidate();
        repaint();
    }

    @Override
    public void onResize() {
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        revalidate();
        repaint();
    }
}
