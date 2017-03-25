package com.alexaegis.progtech.database;

import com.alexaegis.progtech.logic.Updatable;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.sql.*;
import java.util.logging.Logger;

import static com.alexaegis.progtech.Main.dbProperties;
import static com.alexaegis.progtech.Main.sshProperties;
import static com.alexaegis.progtech.misc.ComponentTools.findComponents;
import static com.alexaegis.progtech.window.MainWindow.windowContent;

public final class Connector extends Thread implements Runnable {

    private String dbDriver = dbProperties.getProperty("driver");
    private String jdbcDriver = dbProperties.getProperty("jdbcdriver");
    private String dbProtocol = dbProperties.getProperty("protocol");
    private String dbRemoteHost = dbProperties.getProperty("rhost");
    private int dbRemotePort = Integer.parseInt(dbProperties.getProperty("rport"));
    private String dbLocalHost = dbProperties.getProperty("lhost");
    private int dbLocalPort = Integer.parseInt(dbProperties.getProperty("lport"));
    private String dbSchema = dbProperties.getProperty("schema");
    private String dbUsername = dbProperties.getProperty("username");
    private int dbRefreshInterval = Integer.parseInt(dbProperties.getProperty("refreshinterval"));
    private String sshHost = sshProperties.getProperty("host");
    private int sshPort = Integer.parseInt(sshProperties.getProperty("port"));
    private String sshUsername = sshProperties.getProperty("username");

    private String dbRemoteUrl = dbProtocol + ":" + dbDriver + "://" + dbRemoteHost + ":" + dbRemotePort + "/" + dbSchema;
    private String dbLocalUrl = dbProtocol + ":" + dbDriver + "://" + dbLocalHost + ":" + dbLocalPort + "/" + dbSchema;

    private JSch sch = new JSch();
    private Session session;
    private Connection connection;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private boolean running = false;
    private boolean connected = false;

    public Connector() {

    }

    public Connector(String url, String dbUsername, String dbPassword) {
        this.dbLocalUrl = url;
        this.dbUsername = dbUsername;
        dbProperties.setPropertyObject("password", dbPassword.toCharArray());
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
                logger.info("Port forwarded from " + dbRemoteHost + ":" + dbRemotePort
                + " to " + dbLocalHost + ":" + dbLocalPort);
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
            if(session != null) session.disconnect();
            if(connection != null) connection.close();
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