package com.alexaegis.progtech.window.panels;


import com.alexaegis.progtech.database.Connector;

import javax.swing.*;

import java.awt.*;
import java.sql.SQLException;

import static java.lang.Thread.sleep;

public class ScrollPane extends JScrollPane {

    public ScrollPane(Connector connector, String tableName) throws SQLException {
        try {
            while(!connector.isConnected()) sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataGrid dataGrid = new DataGrid(connector.getConnection(), tableName);
        setViewportView(dataGrid);
        EventQueue.invokeLater(() -> setSize(getParent().getWidth(), getParent().getHeight()));

        setVisible(true);
        revalidate();
        repaint();
    }
}
