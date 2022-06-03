package com.rrpvm.calculator.pojo;

import com.rrpvm.calculator.model.RequestLog;
import com.rrpvm.calculator.pojo.di.interfaces.IDataBaseConnection;
import com.rrpvm.calculator.pojo.di.interfaces.ILogger;
import com.rrpvm.calculator.service.IpResolverService;


import java.sql.PreparedStatement;
import java.sql.SQLException;


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
    private final IDataBaseConnection iDataBaseConnection;
    private final IpResolverService ipResolverService;

    public DataBaseLogger(IDataBaseConnection iDataBaseConnection, IpResolverService ipResolverService) {
        this.iDataBaseConnection = iDataBaseConnection;
        this.ipResolverService = ipResolverService;
    }

    @Override
    public void release() {
        iDataBaseConnection.disconnect();
    }

    @Override
    public void log(RequestLog log) {
        if (iDataBaseConnection.getConnection() == null) return;
        try {
            String jsonResponse = ipResolverService.resolveIp();
            PreparedStatement statement = iDataBaseConnection.getConnection().prepareStatement("INSERT INTO public.logs(expression, result, date, client_ip)VALUES (?,?,?,?)");
            statement.setString(1, log.getExpression());
            statement.setString(2, log.getResult());
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
        iDataBaseConnection.connect();
    }
}
