package com.alexaegis.progtech.window;

import com.alexaegis.progtech.database.Connector;
import com.alexaegis.progtech.logic.controller.KeyboardController;
import com.alexaegis.progtech.window.panels.*;
import com.alexaegis.progtech.window.panels.MenuBar;
import com.github.alexaegis.swing.ResizeableElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.Properties;

import static com.github.alexaegis.swing.ComponentTools.findComponents;

public final class MainWindow extends JFrame implements ComponentListener {

    public static KeyboardController keyboardController = new KeyboardController();
    public static Properties displayProperties = new Properties();
    private final String displayprops = "display.properties";
    private int height = 600;
    private int width = 800;
    private String title = "Title";
    private boolean resizable = true;
    private boolean antialiasing = true;
    public static ContentPane windowContent;

    {
        try {
            displayProperties.load(this.getClass().getClassLoader().getResourceAsStream(displayprops));
            height = Integer.parseInt(displayProperties.getProperty("main.height"));
            width = Integer.parseInt(displayProperties.getProperty("main.width"));
            title = displayProperties.getProperty("main.title");
            resizable = Boolean.parseBoolean(displayProperties.getProperty("resizable"));
            antialiasing = Boolean.parseBoolean(displayProperties.getProperty("antialiasing"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainWindow(Connector connector) throws HeadlessException {
        setTitle(title);
        setSize(width, height);
        setResizable(resizable);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setJMenuBar(new MenuBar());
        windowContent = new ContentPane(connector);
        setContentPane(windowContent);
        addComponentListener(this);
        addKeyListener(keyboardController);
        getRootPane().setFocusable(true);
        requestFocus();
        setVisible(true);
        revalidate();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;
        if(antialiasing) graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        displayProperties.setProperty("main.width", Integer.toString(getWidth() - 16));
        displayProperties.setProperty("main.height", Integer.toString(getHeight() - 32));
        findComponents(this, ResizeableElement.class).forEach(ResizeableElement::onResize);
        revalidate();
        repaint();
        requestFocus();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

}