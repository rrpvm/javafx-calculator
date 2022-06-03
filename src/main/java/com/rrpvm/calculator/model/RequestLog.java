package com.rrpvm.calculator.model;

import java.sql.Timestamp;

public class RequestLog {
    private String expression;
    private String result;
    private Timestamp time;

    public RequestLog(String expression, String result, Timestamp time) {
        this.expression = expression;
        this.result = result;
        this.time = time;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public Timestamp getTime() {
        return time;
    }
}
