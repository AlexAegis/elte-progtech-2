package com.alexaegis.progtech.window.panels;


import com.alexaegis.progtech.database.Connector;

import javax.swing.*;

import java.awt.*;
import java.sql.SQLException;

public class ScrollPane extends JScrollPane {

    public ScrollPane(Connector connector, String tableName) throws SQLException {
        DataGrid dataGrid = new DataGrid(connector.getConnection(), tableName);
        setViewportView(dataGrid);
        EventQueue.invokeLater(() -> setSize(getParent().getWidth(), getParent().getHeight()));

        setVisible(true);
        revalidate();
        repaint();
    }
}
