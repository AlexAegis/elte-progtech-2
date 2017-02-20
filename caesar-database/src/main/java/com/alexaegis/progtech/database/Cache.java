package com.alexaegis.progtech.database;

import com.github.alexaegis.swing.Updatable;

import java.sql.*;
import java.util.Vector;
import java.util.stream.Collectors;

public class Cache implements Updatable {

    private Connector connector;
    private Vector<String> columnNames = new Vector<>();
    private Vector<Vector<Object>> data;
    private String tableName;

    public Cache(Connector connector, String tableName) {
        this.tableName = tableName;
        this.connector = connector;
        update();
    }

    public Vector<Vector<Object>> getData() {
        return data;
    }

    public Vector<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public void update() {
        try(Statement statement = connector.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                columnNames.add(resultSet.getMetaData().getColumnName(i));
            }
            this.columnNames = columnNames;
            Vector<Vector<Object>> freshData = new Vector<>();
            while(resultSet.next()) {
                Vector<Object> row = new Vector<>();
                for(int columnIndex = 1; columnIndex <= resultSet.getMetaData().getColumnCount(); columnIndex++) {
                    row.add(resultSet.getObject(columnIndex));
                }
                freshData.add(row);
            }
            data = freshData;
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return data.stream()
                .map(row -> row.stream().map(Object::toString).collect(Collectors.joining(", ")))
                .collect(Collectors.joining("\n")) + "rowCount: " + data.size() + "columnCount: " + getColumnNames().size();
    }
}