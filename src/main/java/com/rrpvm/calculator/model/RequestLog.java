package com.rrpvm.calculator.model;

import java.sql.Timestamp;

public class RequestLog {
    private String exprission;
    private double result;
    private Timestamp time;

    public RequestLog(String exprission, double result, Timestamp time) {
        this.exprission = exprission;
        this.result = result;
        this.time = time;
    }

    public String getExprission() {
        return exprission;
    }

    public double getResult() {
        return result;
    }

    public Timestamp getTime() {
        return time;
    }
}
