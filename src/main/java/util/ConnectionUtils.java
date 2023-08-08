package main.java.util;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionUtils {

    private static final String DEFAULT_HOST_NAME = "localhost:1433";
    private static final String DEFAULT_USERNAME = "sa";
    private static final String DEFAULT_PASSWORD = "";

    public ConnectionUtils() {
    }

    public static Connection openConnection() {
        try {
            final String connectionURL = "jdbc:sqlserver://" + DEFAULT_HOST_NAME + ';'
                    + "databaseName= master;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true";

            DriverManager.registerDriver(new SQLServerDriver());
            return DriverManager.getConnection(connectionURL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Connection openConnection(String user, String password, String dbName) {
        try {
            final String connectionURL = "jdbc:sqlserver://" + DEFAULT_HOST_NAME + ';'
                    + "databaseName= " + dbName + ';'
                    + "encrypt=true;"
                    + "trustServerCertificate=true";

            DriverManager.registerDriver(new SQLServerDriver());
            return DriverManager.getConnection(connectionURL, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
