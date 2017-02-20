package com.alexaegis.progtech.window.panels;

import com.alexaegis.progtech.Main;
import com.alexaegis.progtech.window.ConnectionWindow;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    public MenuBar() {
        ImageIcon iconBox = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/box32.png"));
        ImageIcon iconWrench = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/wrench32.png"));
        ImageIcon iconBoxDownload = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/boxdownload32.png"));
        ImageIcon iconStop = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/stop32.png"));
        ImageIcon iconSleep = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/sleep32.png"));

        JMenu fileMenu = new JMenu("Database");

        /*JMenu impMenu = new JMenu("Import");

        JMenuItem newsfMi = new JMenuItem("Import newsfeed list...");
        JMenuItem bookmMi = new JMenuItem("Import bookmarks...");
        JMenuItem mailMi = new JMenuItem("Import mail...");

        impMenu.add(newsfMi);
        impMenu.add(bookmMi);
        impMenu.add(mailMi);*/

        JMenuItem miConnect = new JMenuItem("Connect", iconBox);
        JMenuItem miDisconnect = new JMenuItem("Disconnect", iconStop);
        JMenuItem miUpdate = new JMenuItem("Update", iconBoxDownload);
        JMenuItem miOptions = new JMenuItem("Options", iconWrench);

        JMenuItem miExit = new JMenuItem("Exit", iconSleep);

        miExit.setToolTipText("Exit application");

        miExit.addActionListener(e -> System.exit(0));
        miConnect.addActionListener(e -> new ConnectionWindow());
        miUpdate.addActionListener(e -> {
            if(Main.connector.isRunning()) Main.connector.refresh();
        });
        miDisconnect.addActionListener(e -> Main.connector.disconnect());

        fileMenu.add(miConnect);
        fileMenu.add(miDisconnect);
        fileMenu.add(miUpdate);
        fileMenu.addSeparator();
        fileMenu.add(miOptions);
        fileMenu.addSeparator();
        fileMenu.add(miExit);

        add(fileMenu);

        revalidate();
        repaint();
    }
}