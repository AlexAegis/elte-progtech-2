package com.alexaegis.progtech;

import com.alexaegis.progtech.database.Connector;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Main {

    private static final String dbprops = "database.properties";
    private static final String sshprops = "ssh.properties";
    public static final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    public static Properties dbProperties = new EncryptableProperties(encryptor);
    public static Properties sshProperties = new EncryptableProperties(encryptor);

    @Parameter(names = {"--dbdriver", "-dbd"}) static String dbdriver;
    @Parameter(names = {"--jdbcdriver", "-jdbc"}) static String jdbcdriver;
    @Parameter(names = {"--dbprotocol", "-dbprot"}) static String dbprotocol;
    @Parameter(names = {"--dbrhost", "-dbrh", "--dbhost", "-dbh"}) static String dbrhost;
    @Parameter(names = {"--dbremoteport", "--dbrport", "-dbrport", "-dbrp"}) static String dbrport;
    @Parameter(names = {"--dblhost", "-dblh"}) static String dblhost;
    @Parameter(names = {"--dblocalport", "--dblport", "-dblport", "-dblp"}) static String dblport;
    @Parameter(names = {"--dbschema", "-dbsch", "-dbs", "--dbname", "-dbn"}) static String dbschema;
    @Parameter(names = {"--dbusername", "-dbun", "--dbuser", "-dbu"}) static String dbusername;
    @Parameter(names = {"--dbpassword", "-dbpw"}) static String dbpassword;

    @Parameter(names = {"--sshhost", "-sshh"}) static String sshhost;
    @Parameter(names = {"--sshport", "-sshp"}) static String sshport;
    @Parameter(names = {"--sshusername", "-sshun", "--sshuser", "-sshu"}) static String sshusername;
    @Parameter(names = {"--sshpassword", "-ssnpw"}) static String sshpassword;

    public static void main(String[] args) {
        new JCommander(new Main(), args);
        encryptor.setPassword(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        updateProperties();

        Connector connector = new Connector();
    }

    private static void updateProperties() {
        try {
            dbProperties.load(Main.class.getClassLoader().getResourceAsStream(dbprops));
            sshProperties.load(Main.class.getClassLoader().getResourceAsStream(sshprops));

            if(dbdriver != null) dbProperties.setProperty("driver", dbdriver);
            if(jdbcdriver != null) dbProperties.setProperty("jdbcdriver", jdbcdriver);
            if(dbprotocol != null) dbProperties.setProperty("protocol", dbprotocol);
            if(dbrhost != null) dbProperties.setProperty("rhost", dbrhost);
            if(dbrport != null) dbProperties.setProperty("rport", dbrport);
            if(dblhost != null) dbProperties.setProperty("lhost ", dblhost);
            if(dblport != null) dbProperties.setProperty("lport", dblport);
            if(dbschema != null) dbProperties.setProperty("schema", dbschema);
            if(dbusername != null) dbProperties.setProperty("username", dbusername);
            if(dbpassword != null) dbProperties.setProperty("password", encryptor.encrypt(dbpassword));

            if(sshhost != null) sshProperties.setProperty("host", sshhost);
            if(sshport != null) sshProperties.setProperty("port", sshport);
            if(sshusername != null) sshProperties.setProperty("username", sshusername);
            if(sshpassword != null) sshProperties.setProperty("password", encryptor.encrypt(sshpassword));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}