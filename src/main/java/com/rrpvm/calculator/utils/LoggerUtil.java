package com.rrpvm.calculator.utils;

import com.rrpvm.calculator.model.RequestLog;
import com.rrpvm.calculator.service.LoggerService;

public class LoggerUtil {
    private LoggerService loggerService = LoggerService.getInstance();//not-lazy singleton
    private static LoggerUtil loggerUtil = new LoggerUtil();

    private LoggerUtil() {

    }

    public static LoggerUtil getInstance() {
        return loggerUtil;
    }

    public void wakeLogger(RequestLog log) {
        synchronized (loggerService) {
            loggerService.addLogQueue(log);
            loggerService.notify();
        }

    }
}
