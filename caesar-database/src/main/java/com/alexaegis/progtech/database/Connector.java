package com.alexaegis.progtech.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class Connector {

    private final String DBPROPS = "database.properties";
    private final String SSHPROPS = "ssh.properties";
    private final Properties dbProperties;
    private final Properties sshProperties;
    private final String jdbcDriver;
    private final String sshHost;
    private final int sshPort;
    private final String sshUsername;
    private final String sshPassword;
    private final String dbDriver;
    private final String dbProtocol;
    private final String dbHost;
    private final String dbLocalHost;
    private final int dbPort;
    private final int dbLocalPort;
    private final String dbName;
    private final String dbUsername;
    private final String dbPassword;
    private final String dbUrl;
    private final String dbLocalUrl;

    {
        dbProperties = new Properties();
        sshProperties = new Properties();
        dbProperties.load(Connector.class.getClassLoader().getResourceAsStream(DBPROPS));
        sshProperties.load(Connector.class.getClassLoader().getResourceAsStream(SSHPROPS));
        dbDriver = dbProperties.getProperty("driver");
        dbProtocol = dbProperties.getProperty("protocol");
        dbHost = dbProperties.getProperty("host");
        dbLocalHost = dbProperties.getProperty("localhost");
        dbPort = Integer.parseInt(dbProperties.getProperty("port"));
        dbLocalPort = Integer.parseInt(dbProperties.getProperty("localport"));
        dbName = dbProperties.getProperty("databasename");
        dbUsername = dbProperties.getProperty("username");
        dbPassword = dbProperties.getProperty("password"); // TODO prompt me
        jdbcDriver = dbProperties.getProperty("jdbcdriver");
        dbUrl = dbProtocol + ":" + dbDriver + "://" + dbHost + ":" + dbPort + "/" + dbName;
        dbLocalUrl = dbProtocol + ":" + dbDriver + "://" + dbLocalHost + ":" + dbLocalPort + "/" + dbName;
        sshHost = sshProperties.getProperty("host");
        sshPort = Integer.parseInt(sshProperties.getProperty("port"));
        sshUsername = sshProperties.getProperty("username");
        sshPassword = sshProperties.getProperty("password"); // TODO prompt me
    }

    private JSch sshConnection = new JSch();
    private Session session;
    private Logger logger = Logger.getLogger(Connector.class.getName());

    public Connector() throws IOException, SQLException {
        try {
            session = sshConnection.getSession(sshUsername, sshHost, sshPort);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            logger.info("SSH Connected to " + session.getHost());
            session.setPortForwardingL(dbLocalPort, dbLocalHost, dbPort);

            Class.forName(jdbcDriver);

            Connection conn = DriverManager.getConnection(dbLocalUrl, dbUsername, dbPassword);


            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT * FROM Example";

            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next())
            {
                String firstName = rs.getString("Name");

                // print the results
                System.out.format("%s \n", firstName);
            }
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