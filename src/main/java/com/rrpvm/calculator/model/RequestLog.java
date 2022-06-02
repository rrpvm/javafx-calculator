package com.rrpvm.calculator.model;

import java.util.Date;

/*'https://api.ipify.org?format=json'*/
@Deprecated
public class RequestLog {
    private String exprission;
    private String ip;
    private Date time;

    public RequestLog(String exprission, String ip, Date time) {
        this.exprission = exprission;
        this.ip = ip;
        this.time = time;
    }

    public String getExprission() {
        return exprission;
    }

    public String getIp() {
        return ip;
    }

    public Date getTime() {
        return time;
    }
}
