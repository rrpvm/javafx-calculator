package com.rrpvm.calculator.service;

import com.rrpvm.calculator.model.RequestLog;
import com.rrpvm.calculator.pojo.DataBaseLogger;
import com.rrpvm.calculator.pojo.connectors.PostgresDataBaseConnector;
import com.rrpvm.calculator.pojo.di.interfaces.ILogger;

import java.util.concurrent.LinkedBlockingQueue;

public class LoggerService extends Thread {
    private static LoggerService instance;
    private ILogger logger;
    private LinkedBlockingQueue<RequestLog> logQueue;
    private volatile boolean bAlive;

    public static LoggerService getInstance() {
        if (instance == null) {
            instance = new LoggerService();
        }
        return instance;
    }

    private LoggerService() {
        logger = new DataBaseLogger(new PostgresDataBaseConnector(), new IpResolverService());
        logQueue = new LinkedBlockingQueue<>();
        bAlive = true;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("logger service is running...");
        logger.setup();
        while (bAlive) {
            try {
                synchronized (this) {
                    if (this.logQueue.isEmpty()) {
                        wait();//sleep until
                        if (!bAlive) break;
                    }
                }
                RequestLog log = this.logQueue.take();//sync method
                new Thread(() -> logger.log(log)).start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.release();
        System.out.println("logger service is released\t" + Thread.currentThread().getName());
    }

    public void addLogQueue(RequestLog log) {
        try {
            this.logQueue.put(log);//sync method
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void shutdown() {
        this.bAlive = false;
    }
}
