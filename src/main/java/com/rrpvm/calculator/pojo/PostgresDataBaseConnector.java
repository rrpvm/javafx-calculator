package com.rrpvm.calculator.pojo;

import com.rrpvm.calculator.pojo.di.interfaces.IDataBaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDataBaseConnector implements IDataBaseConnection {
    private String CONNECTION_URL = "jdbc:postgresql://localhost:5432/calculator_logs";
    private String DB_USERNAME = "postgres";
    private String DB_PASSWORD = "123";
    private Connection connection;

    @Override
    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("db connected");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null) connection.close();
            System.out.println("db closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }
}
