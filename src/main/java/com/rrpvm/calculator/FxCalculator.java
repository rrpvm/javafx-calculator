package com.rrpvm.calculator;

import com.rrpvm.calculator.service.LoggerService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FxCalculator extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FxCalculator.class.getResource("calculus.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setResizable(false);
        stage.setTitle("calculator");
        stage.setScene(scene);
        stage.show();
        new Thread(LoggerService.getInstance()).start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        LoggerService loggerService = LoggerService.getInstance();
        synchronized (loggerService) {
            loggerService.notify();
            loggerService.shutdown();
        }
    }

    public static void main(String[] args) {
        launch();
    }


}