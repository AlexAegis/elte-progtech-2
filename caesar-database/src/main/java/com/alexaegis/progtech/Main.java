package com.alexaegis.progtech;

import com.alexaegis.progtech.database.Connector;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        Connector connector = new Connector();
        connector.connect();
        System.out.println(connector.toString());
    }
}