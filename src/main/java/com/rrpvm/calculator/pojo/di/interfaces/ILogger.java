package com.rrpvm.calculator.pojo.di.interfaces;

import com.rrpvm.calculator.model.RequestLog;


public interface ILogger {
    void log(RequestLog requestLog);

    void setup();

    void release();
}
