package com.alexaegis.progtech.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;

import static com.alexaegis.progtech.Main.dbProperties;
import static com.alexaegis.progtech.Main.encryptor;
import static com.alexaegis.progtech.Main.sshProperties;

public class Connector {

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
    private final String sshHost = sshProperties.getProperty("host");
    private final int sshPort = Integer.parseInt(sshProperties.getProperty("port"));
    private final String sshUsername = sshProperties.getProperty("username");
    private final String sshPassword = sshProperties.getProperty("password");

    private final String dbRemoteUrl = dbProtocol + ":" + dbDriver + "://" + dbRemoteHost + ":" + dbRemotePort + "/" + dbSchema;
    private final String dbLocalUrl = dbProtocol + ":" + dbDriver + "://" + dbLocalHost + ":" + dbLocalPort + "/" + dbSchema;

    private JSch sch = new JSch();
    private Session session;
    private Logger logger = Logger.getLogger(Connector.class.getName());

    public Connector() {
        try {
            session = sch.getSession(sshUsername, sshHost, sshPort);
            session.setPassword(encryptor.decrypt(sshPassword));
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            logger.info("SSH Connected to " + session.getHost());
            session.setPortForwardingL(dbLocalPort, dbRemoteHost, dbRemotePort);

            Class.forName(jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(dbLocalUrl, dbUsername, encryptor.decrypt(dbPassword));

            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT * FROM Example";

            // create the java statement
            Statement st = conn.createStatement();
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }

            conn.close();
            rs.close();
            st.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            session.disconnect();
        }

    }

    public void connect() {

    }

    @Override
    public String toString() {
        return dbLocalUrl;
    }
}