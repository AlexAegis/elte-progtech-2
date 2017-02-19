package com.alexaegis.progtech.database;

import com.github.alexaegis.swing.ResizeableElement;
import com.github.alexaegis.swing.Updatable;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.sql.*;
import java.util.logging.Logger;

import static com.alexaegis.progtech.Main.dbProperties;
import static com.alexaegis.progtech.Main.encryptor;
import static com.alexaegis.progtech.Main.sshProperties;
import static com.alexaegis.progtech.window.Window.windowContent;
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
    private final String dbPassword = dbProperties.getProperty("password");
    private final int dbRefreshInterval = Integer.parseInt(dbProperties.getProperty("refreshinterval"));
    private final String sshHost = sshProperties.getProperty("host");
    private final int sshPort = Integer.parseInt(sshProperties.getProperty("port"));
    private final String sshUsername = sshProperties.getProperty("username");
    private final String sshPassword = sshProperties.getProperty("password");
    private final boolean usessh = Boolean.parseBoolean(sshProperties.getProperty("usessh"));

    private final String dbRemoteUrl = dbProtocol + ":" + dbDriver + "://" + dbRemoteHost + ":" + dbRemotePort + "/" + dbSchema;
    private final String dbLocalUrl = dbProtocol + ":" + dbDriver + "://" + dbLocalHost + ":" + dbLocalPort + "/" + dbSchema;

    private JSch sch = new JSch();
    private Session session;
    private Connection connection;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private boolean running = false;
    private boolean connected = false;

    public Connector() {
        try {
            if(usessh) {
                session = sch.getSession(sshUsername, sshHost, sshPort);
                session.setPassword(encryptor.decrypt(sshPassword));
                session.setConfig("StrictHostKeyChecking", "no");
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            if(usessh) {
                session.connect();
                logger.info("SSH Connected to " + session.getHost());
                session.setPortForwardingL(dbLocalPort, dbRemoteHost, dbRemotePort);
            }
            connection = DriverManager.getConnection(dbLocalUrl, dbUsername, encryptor.decrypt(dbPassword));
            connected = true;
        } catch (JSchException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String query) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        //st.close();
        return rs;
    }

    public void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }

    private void refresh() {
        if(windowContent != null) findComponents(windowContent, Updatable.class).forEach(Updatable::update);
        logger.info("Database refreshed");
    }

    public void disconnect() {
        try {
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
        connect();
        while(running) {
            try {
                sleep(1000 * dbRefreshInterval);
                refresh();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        disconnect();
    }

    public void stopConnection() {
        logger.info("Connection stopped");
        running = false;
    }

    @Override
    public String toString() {
        return dbLocalUrl;
    }

    public boolean isConnected() {
        return connected;
    }
}