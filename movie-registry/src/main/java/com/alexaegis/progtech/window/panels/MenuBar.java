package com.alexaegis.progtech.window.panels;

import com.alexaegis.progtech.Main;
import com.alexaegis.progtech.database.MovieHandler;
import com.alexaegis.progtech.window.ConnectionWindow;
import com.alexaegis.progtech.window.NewMovieWindow;

import javax.swing.*;

import static com.alexaegis.progtech.Main.connector;
import static com.alexaegis.progtech.window.MainWindow.windowContent;

public class MenuBar extends JMenuBar {

    public MenuBar() {
        ImageIcon iconBox = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/box32.png"));
        ImageIcon iconWrench = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/wrench32.png"));
        ImageIcon iconBoxDownload = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/boxdownload32.png"));
        ImageIcon iconStop = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/stop32.png"));
        ImageIcon iconSleep = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/sleep32.png"));
        ImageIcon iconShop = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/shoppingcart32.png"));
        ImageIcon iconRight = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/turnright32.png"));
        ImageIcon iconLeft = new ImageIcon(MenuBar.class.getClassLoader().getResource("icons/turnright32.png")); //TODO new icon

        JMenu fileMenu = new JMenu("Connection");

        JMenuItem miConnect = new JMenuItem("Connect", iconBox);
        JMenuItem miDisconnect = new JMenuItem("Disconnect", iconStop);
        JMenuItem miUpdate = new JMenuItem("Update", iconBoxDownload);

        JMenuItem miExit = new JMenuItem("Exit", iconSleep);

        miExit.setToolTipText("Exit application");

        miConnect.addActionListener(e -> new ConnectionWindow());
        miDisconnect.addActionListener(e -> Main.connector.disconnect());
        miUpdate.addActionListener(e -> {
            if(Main.connector.isRunning()) Main.connector.refresh();
        });
        miExit.addActionListener(e -> {
            Main.connector.disconnect();
            System.exit(0);
        });

        fileMenu.add(miConnect);
        fileMenu.add(miDisconnect);
        fileMenu.add(miUpdate);
        fileMenu.addSeparator();
        fileMenu.add(miExit);

        JMenu databaseMenu = new JMenu("Database");

        JMenuItem miInitDB = new JMenuItem("Initialize Database", iconShop);
        JMenuItem miRestoreDB = new JMenuItem("Restore Database", iconShop);
        JMenuItem miBackupDB = new JMenuItem("Backup Database", iconShop);

        databaseMenu.add(miInitDB);
        databaseMenu.add(miRestoreDB);
        databaseMenu.add(miBackupDB);

        JMenu movieMenu = new JMenu("Movies");
        JMenuItem miAddMovie = new JMenuItem("Add Movie", iconShop);
        JMenuItem miShowMovies = new JMenuItem("Show Movies", iconShop);

        miShowMovies.addActionListener(e -> windowContent.getScrollPane().setMovieGrid());

        miAddMovie.addActionListener(e -> new NewMovieWindow());
        movieMenu.add(miAddMovie);
        movieMenu.addSeparator();
        movieMenu.add(miShowMovies);

        JMenu leasesMenu = new JMenu("Leases");
        JMenuItem miListLeases = new JMenuItem("List Leases", iconShop);
        leasesMenu.add(miListLeases);

        miListLeases.addActionListener(e -> windowContent.getScrollPane().setLeaseGrid());

        JButton panicButton = new JButton("Panic");

        panicButton.addActionListener(e -> {
            MovieHandler movieHandler = new MovieHandler(connector);
            movieHandler.panic();
            Main.connector.refresh();
        });

        add(fileMenu);
        add(databaseMenu);
        add(movieMenu);
        add(leasesMenu);
        add(panicButton);

        revalidate();
        repaint();
    }
}