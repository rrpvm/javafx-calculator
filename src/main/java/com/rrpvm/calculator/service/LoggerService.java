package com.rrpvm.calculator.service;

import com.rrpvm.calculator.pojo.DataBaseLogger;
import com.rrpvm.calculator.pojo.di.interfaces.ILogger;

public class LoggerService extends Thread {
    private static LoggerService instance;
    private ILogger logger;
    private boolean bAlive;
    private String expression;
    private Double result;

    private LoggerService() {
        logger = new DataBaseLogger();
        bAlive = true;
        expression = "";
    }

    @Override
    public void run() {
        super.run();
        logger.setup();
        while (bAlive) {
            try {
                synchronized (this) {
                    wait();
                    if (!bAlive) break;
                }
                logger.log(expression,result);
                expression = null;
                result = 0.0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.release();
        System.out.println("released");
    }

    public synchronized void setExpression(String expression) {
        this.expression = expression;

    }

    public synchronized void setResult(Double result) {
        this.result = result;
    }

    public static LoggerService getInstance() {
        if (instance == null) {
            instance = new LoggerService();
        }
        return instance;
    }

    public void shutdown() {
        this.bAlive = false;
    }
}
