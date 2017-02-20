package com.alexaegis.progtech.database;

import com.github.alexaegis.swing.Updatable;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.sql.*;
import java.util.logging.Logger;

import static com.alexaegis.progtech.Main.dbProperties;
import static com.alexaegis.progtech.Main.sshProperties;
import static com.alexaegis.progtech.window.MainWindow.windowContent;
import static com.github.alexaegis.swing.ComponentTools.findComponents;

public final class Connector extends Thread implements Runnable {

    private final String dbDriver = dbProperties.getProperty("driver");
    private final String jdbcDriver = dbProperties.getProperty("jdbcdriver");
    private final String dbProtocol = dbProperties.getProperty("protocol");
    private final String dbRemoteHost = dbProperties.getProperty("rhost");
    private final int dbRemotePort = Integer.parseInt(dbProperties.getProperty("rport"));
    private final String dbLocalHost = dbProperties.getProperty("lhost");
    private final int dbLocalPort = Integer.parseInt(dbProperties.getProperty("lport"));
    private final String dbSchema = dbProperties.getProperty("schema");
    private final String dbUsername = dbProperties.getProperty("username");
    private final int dbRefreshInterval = Integer.parseInt(dbProperties.getProperty("refreshinterval"));
    private final String sshHost = sshProperties.getProperty("host");
    private final int sshPort = Integer.parseInt(sshProperties.getProperty("port"));
    private final String sshUsername = sshProperties.getProperty("username");

    private final String dbRemoteUrl = dbProtocol + ":" + dbDriver + "://" + dbRemoteHost + ":" + dbRemotePort + "/" + dbSchema;
    private final String dbLocalUrl = dbProtocol + ":" + dbDriver + "://" + dbLocalHost + ":" + dbLocalPort + "/" + dbSchema;

    private JSch sch = new JSch();
    private Session session;
    private Connection connection;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private boolean running = false;
    private boolean connected = false;

    public Connector() {

    }

    public void connect(boolean usessh) {
        try {
            if(usessh) {
                session = sch.getSession(sshUsername, sshHost, sshPort);
                session.setPassword(String.copyValueOf((char[]) sshProperties.getPropertyObject("password")));
                sshProperties.destroyPasswordProperty("password");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                logger.info("SSH Connected to " + session.getHost());
                session.setPortForwardingL(dbLocalPort, dbRemoteHost, dbRemotePort);
            }
            connection = DriverManager.getConnection(dbLocalUrl,
                    dbUsername,
                    String.copyValueOf((char[]) dbProperties.getPropertyObject("password")));
            dbProperties.destroyPasswordProperty("password");
            logger.info("Database Connected to " + connection.getMetaData().getURL());
            connected = true;
            run();
        } catch (JSchException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void refresh() {
        if(windowContent != null) findComponents(windowContent, Updatable.class).forEach(Updatable::update);
        logger.info("Database refreshed");
    }

    public void disconnect() {
        try {
            running = false;
            logger.info("Connection stopped");
            session.disconnect();
            connection.close();
            connected = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                sleep(1000 * dbRefreshInterval);
                if(running) refresh();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return dbLocalUrl;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isRunning() {
        return running;
    }
}