package com.rrpvm.calculator.pojo;

import com.rrpvm.calculator.pojo.di.interfaces.ILogger;

import java.sql.*;

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
            System.out.println("closed db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(String expression, Double result){
        if (this.connection == null) return;
        try{
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO logs(expression, result, date, client_ip)VALUES (?,?,?,NULL)");
            statement.setString(1,expression);
            statement.setDouble(2,result);
           // statement.setDate(new Date());

        }
        catch (SQLException e){

        }

        statement.setString(1, );
        System.out.println(expression);
    }

    @Override
    public void setup() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
