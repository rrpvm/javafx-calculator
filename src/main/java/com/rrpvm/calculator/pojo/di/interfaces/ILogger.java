package com.rrpvm.calculator.pojo.di.interfaces;

public interface ILogger {
    void log(String expression, Double result);
    void setup();
    void release();
}
