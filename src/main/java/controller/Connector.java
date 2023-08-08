package main.java.controller;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.util.ConnectionUtils;

public class Connector {

    private Connection connection;

    private String user;
    private String password;
    private String dbName;

    public Connector() {
        this.connection = ConnectionUtils.openConnection();
    }

    public Connector(String user, String password, String dbName) {
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.connection = new ConnectionUtils()
                .openConnection(user, password, dbName);
    }

    public boolean isConnected() {
        connection = new ConnectionUtils()
                .openConnection(this.user, this.password, this.dbName);
        return connection != null;
    }

    public List<List<String>> getDataFrom(String tableName) {
        // new 2d list to return data got from table in sql server
        List<List<String>> table = new ArrayList<>();
        try {
            final String query = "use " + this.dbName
                    + " select * from " + tableName;
            final Statement state = this.connection.createStatement();
            final ResultSet res = state.executeQuery(query);
            final ResultSetMetaData rsmd = res.getMetaData();
            int column_count = rsmd.getColumnCount();

            while (res.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= column_count; i++) {
                    row.add(res.getString(i));
                }
                table.add(row);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return table;
    }

    public List<String> getColumnsName(String tableName) {
        List<String> columns = new ArrayList<>();
        try {
            final String query = "use " + this.dbName
                    + " select * from " + tableName;
            final Statement state = this.connection.createStatement();
            final ResultSet res = state.executeQuery(query);
            final ResultSetMetaData rsmd = res.getMetaData();

            int column_count = rsmd.getColumnCount();
            for (int i = 1; i <= column_count; i++) {
                columns.add(rsmd.getColumnName(i));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return columns;
    }

    public List<List<String>> callProc(String procName, String... values) {
        StringBuilder sb = new StringBuilder("");

        int leng = values.length;

        if (leng <= 0) {
            sb.append("");
        } else if (leng != 1) {
            sb.append("?");
            for (int i = 1; i <= leng; i++) {
                sb.append(", ?");
            }
        } else {
            sb.append("?");
        }
        String procedureQuery = "{call " + procName + "(" + sb.toString() + ")}";
        List<List<String>> executedTable = new ArrayList<>();

        try ( CallableStatement cstm = this.connection.prepareCall(procedureQuery);) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                cstm.setString(i + 1, value);
            }

            ResultSet res = cstm.executeQuery();
            ResultSetMetaData rsmd = res.getMetaData();

            int column_count = rsmd.getColumnCount();
            if (column_count <= 0) {
                return new ArrayList<>();
            }

            while (res.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= column_count; i++) {
                    row.add(res.getString(i));
                }
                executedTable.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executedTable;
    }

    public List<List<String>> getDataFrom(String tableName, String filter) {
        // new 2d list to return data got from table in sql server
        List<List<String>> table = new ArrayList<>();
        try {
            final String query = "use " + this.dbName
                    + " select * from " + tableName
                    + " where " + filter;
            final Statement state = this.connection.createStatement();
            final ResultSet res = state.executeQuery(query);
            final ResultSetMetaData rsmd = res.getMetaData();
            int column_count = rsmd.getColumnCount();

            while (res.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= column_count; i++) {
                    row.add(res.getString(i));
                }
                table.add(row);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return table;
    }
}
