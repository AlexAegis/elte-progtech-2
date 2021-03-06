package com.alexaegis.progtech.window.panels;


import com.alexaegis.progtech.database.Connector;
import com.alexaegis.progtech.logic.ResizeableElement;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import static com.alexaegis.progtech.window.MainWindow.displayProperties;
import static java.lang.Thread.sleep;

public class ContentPane extends JLayeredPane implements ResizeableElement {

    private ScrollPane scrollPane;

    public ContentPane(Connector connector) {
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        new Thread(() -> {

            try {
                while(!connector.isConnected()) sleep(250);
                scrollPane = new ScrollPane(connector);
                add(scrollPane);
            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
        setLayout(null);
        setBackground(new Color(130, 162, 182, 255));
        setVisible(true);
        revalidate();
        repaint();
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    @Override
    public void onResize() {
        setSize(Integer.parseInt(displayProperties.getProperty("main.width")), Integer.parseInt(displayProperties.getProperty("main.height")));
        revalidate();
        repaint();
    }
}