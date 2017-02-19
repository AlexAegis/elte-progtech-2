package com.alexaegis.progtech.window.panels;


import com.github.alexaegis.swing.ResizeableElement;

import javax.swing.*;
import java.awt.*;

import static com.alexaegis.progtech.window.Window.displayProperties;

public class ContentPane extends JLayeredPane implements ResizeableElement {

    public ContentPane() {
        setSize(Integer.parseInt(displayProperties.getProperty("width")), Integer.parseInt(displayProperties.getProperty("height")));
        //int i = (int) displayProperties.get("width");
        //System.out.println(i);
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