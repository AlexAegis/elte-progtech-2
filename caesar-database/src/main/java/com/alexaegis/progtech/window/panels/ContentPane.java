package com.alexaegis.progtech.window.panels;


import com.github.alexaegis.swing.ResizeableElement;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

import static com.alexaegis.progtech.Main.connector;
import static com.alexaegis.progtech.window.Window.displayProperties;
import static java.lang.Thread.sleep;

public class ContentPane extends JLayeredPane implements ResizeableElement {

    public ContentPane() {
        setSize(Integer.parseInt(displayProperties.getProperty("width")), Integer.parseInt(displayProperties.getProperty("height")));
        new Thread(() -> {
            try {
                while(!connector.isConnected()) sleep(250);
                add(new DataGrid());
            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
        setLayout(null);
        setBackground(new Color(130, 162, 182, 255));
    }

    @Override
    public void onResize() {
        setSize(Integer.parseInt(displayProperties.getProperty("width")), Integer.parseInt(displayProperties.getProperty("height")));
        revalidate();
        repaint();
    }
}