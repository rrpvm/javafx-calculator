package com.rrpvm.calculator;

import com.rrpvm.calculator.service.LoggerService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FxCalculator extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FxCalculator.class.getResource("calculus.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setResizable(false);
        stage.setTitle("calculator");
        stage.setScene(scene);
        stage.show();

       /* for (int i = 0; i < 2; i++) { нам попросту не нужно 2 потока записи(хотя так быстрее)
            Thread thread = new Thread(LoggerService.getInstance());
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setDaemon(true);
            thread.start();
        }*/
        LoggerService.getInstance().setPriority(Thread.MIN_PRIORITY);
        LoggerService.getInstance().setDaemon(true);
        LoggerService.getInstance().start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        LoggerService loggerService = LoggerService.getInstance();
        synchronized (loggerService) {
            loggerService.notifyAll();
            loggerService.shutdown();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}