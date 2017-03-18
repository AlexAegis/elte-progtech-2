package com.alexaegis.progtech.database;

import com.alexaegis.progtech.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.Statement;

import static java.lang.Thread.sleep;

public class MovieInserterTest {

    Connector connector;

    @Before
    public void beforeTests() throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        new Main().updateProperties();
        String executionPath = System.getProperty("user.dir");
        String dbUrl = "jdbc:derby:" + executionPath + "/src/main/resources/database/moviedata";
        dbUrl = dbUrl.replace("\\", "/");
        connector = new Connector(dbUrl, "asd", "asd");
        new Thread(() -> connector.connect(false)).start();
        while(!connector.isConnected()) {
            sleep(500);
            System.out.println("Connecting..");
        }
    }

    @Test
    public void mainTest() throws Exception {
        System.out.println("AD");
        Statement statement = connector.getConnection().createStatement();

        PreparedStatement stt = connector.getConnection().prepareStatement("update Students set ic = ?, name = ?, level = ?, programmecode = ?, year = ? where id = ");

        stt.setString(1, "CREATE TABLE movies\n" +
                "(\n" +
                "    IDEN INT(11),\n" +
                "    Title VARCHAR(100),\n" +
                "    `Release` DATE\n" +
                ");");
        stt.executeUpdate();
        statement.execute("commit;");
        statement.close();
    }

    @After
    public void afterTests() {
        connector.disconnect();
    }

}