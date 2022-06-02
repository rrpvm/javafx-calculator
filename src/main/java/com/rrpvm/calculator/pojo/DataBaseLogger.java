package com.rrpvm.calculator.pojo;

import com.rrpvm.calculator.model.RequestLog;
import com.rrpvm.calculator.pojo.di.interfaces.ILogger;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Scanner;

/* statement.execute("create table logs\n" +
                    "(\n" +
                    "    expression text,\n" +
                    "    id         serial\n" +
                    "            primary key,\n" +
                    "    result     double precision,\n" +
                    "    date       date not null,\n" +
                    "    client_ip  text not null\n" +
                    ");\n" +
                    "\n" +
                    "create unique index logs_id_uindex\n" +
                    "    on logs (id);\n" +
                    "\n");*/
public class DataBaseLogger implements ILogger {
    private String CONNECTION_URL = "jdbc:postgresql://localhost:5432/calculator_logs";
    private String DB_USERNAME = "postgres";
    private String DB_PASSWORD = "123";
    private Connection connection;

    @Override
    public void release() {
        try {
            if (connection != null) connection.close();
            System.out.println("db closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(RequestLog log) {
        if (this.connection == null) return;
        try {
            String jsonResponse = null;
            try {
                jsonResponse = getIpAddress();
                jsonResponse = jsonResponse.substring(7, jsonResponse.length() - 2);//get ip line
            } catch (UnknownHostException unknownHostException) {
                jsonResponse = "localhost";
            }
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO public.logs(expression, result, date, client_ip)VALUES (?,?,?,?)");
            statement.setString(1, log.getExprission());
            statement.setDouble(2, log.getResult());
            statement.setTimestamp(3, log.getTime());
            statement.setString(4, jsonResponse);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setup() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("db connected");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getIpAddress() throws UnknownHostException {
        URL url = null;
        HttpURLConnection connection = null;
        Scanner scanner = null;
        try {
            url = new URL("https://api.ipify.org?format=json");
            connection = (HttpURLConnection) url.openConnection();
            scanner = new Scanner(connection.getInputStream());
            if (scanner.hasNextLine()) return scanner.nextLine();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException u) {
            throw u;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null)
                scanner.close();
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }
}
