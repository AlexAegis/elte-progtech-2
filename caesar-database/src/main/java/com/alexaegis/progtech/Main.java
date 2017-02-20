package com.alexaegis.progtech;

import com.alexaegis.progtech.database.Connector;
import com.alexaegis.progtech.window.Window;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public final class Main {

    private final String dbprops = "database.properties";
    private final String sshprops = "ssh.properties";
    public static final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    public static Properties dbProperties = new EncryptableProperties(encryptor);
    public static Properties sshProperties = new EncryptableProperties(encryptor);

    @Parameter(names = {"--dbdriver", "-dbd"})                                  private String dbdriver;
    @Parameter(names = {"--jdbcdriver", "-jdbc"})                               private String jdbcdriver;
    @Parameter(names = {"--dbprotocol", "-dbprot"})                             private String dbprotocol;
    @Parameter(names = {"--dbrhost", "-dbrh", "--dbhost", "-dbh"})              private String dbrhost;
    @Parameter(names = {"--dbremoteport", "--dbrport", "-dbrport", "-dbrp"})    private String dbrport;
    @Parameter(names = {"--dblhost", "-dblh"})                                  private String dblhost;
    @Parameter(names = {"--dblocalport", "--dblport", "-dblport", "-dblp"})     private String dblport;
    @Parameter(names = {"--dbschema", "-dbsch", "-dbs", "--dbname", "-dbn"})    private String dbschema;
    @Parameter(names = {"--dbusername", "-dbun", "--dbuser", "-dbu"})           private String dbusername;
    @Parameter(names = {"--dbpassword", "-dbpw"})                               private String dbpassword;
    @Parameter(names = {"--dbrefreshinterval", "-dbri"})                        private String dbrefreshinterval;
    @Parameter(names = {"--dbtablenames", "-dbtn"})                             private String dbtablenames;
    @Parameter(names = {"--sshhost", "-sshh"})                                  private String sshhost;
    @Parameter(names = {"--sshport", "-sshp"})                                  private String sshport;
    @Parameter(names = {"--sshusername", "-sshun", "--sshuser", "-sshu"})       private String sshusername;
    @Parameter(names = {"--sshpassword", "-ssnpw"})                             private String sshpassword;
    @Parameter(names = {"--usessh", "-us"})                                     private String usessh;

    public static void main(String[] args) throws SQLException {
        encryptor.setPassword(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        Main main = new Main();
        new JCommander(main, args);
        main.updateProperties();
        Connector connector = new Connector();
        connector.start();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            EventQueue.invokeLater(() -> new Window(connector));
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void updateProperties() {
        try {
            dbProperties.load(Main.class.getClassLoader().getResourceAsStream(dbprops));
            sshProperties.load(Main.class.getClassLoader().getResourceAsStream(sshprops));
            if(dbdriver != null)            dbProperties.setProperty("driver", dbdriver);
            if(jdbcdriver != null)          dbProperties.setProperty("jdbcdriver", jdbcdriver);
            if(dbprotocol != null)          dbProperties.setProperty("protocol", dbprotocol);
            if(dbrhost != null)             dbProperties.setProperty("rhost", dbrhost);
            if(dbrport != null)             dbProperties.setProperty("rport", dbrport);
            if(dblhost != null)             dbProperties.setProperty("lhost ", dblhost);
            if(dblport != null)             dbProperties.setProperty("lport", dblport);
            if(dbschema != null)            dbProperties.setProperty("schema", dbschema);
            if(dbusername != null)          dbProperties.setProperty("username", dbusername);
            if(dbpassword != null)          dbProperties.setProperty("password", encryptor.encrypt(dbpassword));
            if(dbrefreshinterval != null)   dbProperties.setProperty("refreshinterval", dbrefreshinterval);
            if(dbtablenames != null)        dbProperties.setProperty("tablenames", dbtablenames);
            if(sshhost != null)             sshProperties.setProperty("host", sshhost);
            if(sshport != null)             sshProperties.setProperty("port", sshport);
            if(sshusername != null)         sshProperties.setProperty("username", sshusername);
            if(sshpassword != null)         sshProperties.setProperty("password", encryptor.encrypt(sshpassword));
            if(usessh != null)              sshProperties.setProperty("usessh", usessh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}