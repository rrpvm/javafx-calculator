package com.rrpvm.calculator.pojo.di.interfaces;

import java.sql.Connection;

public interface IDataBaseConnection {
    void connect();
    void disconnect();
    Connection getConnection();
}
