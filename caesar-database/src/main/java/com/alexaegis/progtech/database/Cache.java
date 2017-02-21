package com.alexaegis.progtech.database;

import com.github.alexaegis.swing.Updatable;

import java.sql.*;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Cache implements Updatable {

    protected Connector connector;
    protected Vector<String> columnNames = new Vector<>();
    protected Vector<Vector<Object>> data;
    protected String query;

    public Cache(Connector connector) {
        this.connector = connector;
    }

    public Vector<Vector<Object>> getData() {
        return data;
    }

    public Vector<String> getColumnNames() {
        return columnNames;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Object> getColumn(int i) {
        return data.stream().map(objects -> objects.get(i)).collect(Collectors.toList());
    }

    @Override
    public void update() {
        try(Statement statement = connector.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
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