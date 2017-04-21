package com.alexaegis.progtech.database;


import com.alexaegis.progtech.logic.SecureProperties;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ConnectorTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private static final String JDBCURL = "jdbc:derby:src/main/resources/database/localdb;create=true";
    private static Connection connection;
    private static SecureProperties properties = new SecureProperties();

    @BeforeAll
    public static void setUp() throws Exception {
        connection = DriverManager.getConnection(JDBCURL);
        properties.load(ConnectorTest.class.getClassLoader().getResourceAsStream("database.properties"));
    }

    @Test
    public void createAllSchemesTest() throws Exception {
        dropTables();
        try(Stream<Path> pathStream = Files.walk(Paths.get("src/main/resources/database/schemas"))) {
            pathStream.forEach(path -> {
                if (Files.isRegularFile(path)) {
                    try {
                        String content = new Scanner(path).useDelimiter("\\Z").next();
                        //logger.info("File found: " + path.toString() + "\nFile content: \n" + content);
                        connection.createStatement().execute(content);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Test
    public void fillAllSchemesTest() throws Exception {
        try(Stream<Path> pathStream = Files.walk(Paths.get("src/main/resources/database/data"))) {
            pathStream.forEach(path -> {
                if (Files.isRegularFile(path)) {
                    System.out.println(path.toString());
                    try (Scanner scanner = new Scanner(path)) {
                        while (scanner.hasNextLine()) {
                            connection.createStatement().execute(scanner.nextLine().replace(';', ' '));
                        }
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static void dropTables() throws SQLException {
        for (String tableName : properties.getProperty("tablenames").split(";")) {
            System.out.println(tableName);
            try {
                connection.createStatement().execute("DROP TABLE " + tableName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterAll
    public static void tearDown() throws Exception {
        //dropTables();
        connection.close();
    }
}
