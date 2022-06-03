package com.rrpvm.calculator.pojo;

import com.rrpvm.calculator.pojo.di.interfaces.IDataBaseConnection;
import com.rrpvm.calculator.pojo.di.interfaces.ILogReader;
import com.rrpvm.calculator.service.IpResolverService;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DataBaseReader implements ILogReader {
    private final IDataBaseConnection iDataBaseConnection;
    private final IpResolverService ipResolverService;

    public DataBaseReader(IDataBaseConnection iDataBaseConnection, IpResolverService ipResolverService) {
        this.iDataBaseConnection = iDataBaseConnection;
        this.ipResolverService = ipResolverService;
    }

    @Override
    public List<Pair<String, String>> readClientLogs() {
        Connection connection = this.iDataBaseConnection.getConnection();
        LinkedList<Pair<String, String>> result = new LinkedList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select expression, result from public.logs where client_ip = ? order by date desc limit 5");
            preparedStatement.setString(1, ipResolverService.resolveIp());
            var list = preparedStatement.executeQuery();
            while (list.next()) {
                result.addLast(new Pair<String, String>(list.getString(1), list.getString(2)));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void setup() {
        this.iDataBaseConnection.connect();
    }

    @Override
    public void release() {
        this.iDataBaseConnection.disconnect();
    }
}
