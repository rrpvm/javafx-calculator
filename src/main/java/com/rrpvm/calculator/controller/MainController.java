package com.rrpvm.calculator.controller;

import com.rrpvm.calculator.service.CalculatorService;
import com.rrpvm.calculator.service.LoggerService;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;

public class MainController {
    private CalculatorService calculatorService = CalculatorService.getInstance();
    @FXML
    private TextField expressionArea;

    @FXML
    private void onInput(ActionEvent event) {
        String txt = ((Button) event.getSource()).getText();
        expressionArea.setText(expressionArea.getText() + txt);
    }

    @FXML
    private void onClear(MouseEvent mouseEvent) {
        expressionArea.clear();
    }

    @FXML
    private void onErase(MouseEvent mouseEvent) {
        if (expressionArea.getText().isEmpty()) return;
        expressionArea.setText(expressionArea.getText(0, expressionArea.getText().length() - 1));
    }

    @FXML
    private void onCalculateClicked(MouseEvent mouseEvent) {
        try {
            double result = calculatorService.calculate(this.expressionArea.getText());
            synchronized (LoggerService.getInstance()) {
                LoggerService.getInstance().notify();
                LoggerService.getInstance().setExpression(this.expressionArea.getText());
                LoggerService.getInstance().setResult(result);
            }
            this.expressionArea.setText(Double.toString(result));
        } catch (IllegalArgumentException exception) {
            // this.expressionArea.setText();
            // exception.printStackTrace();
            //вообще - подстветить поле, что нужно еще что-то
        } catch (ArithmeticException e) {
            this.expressionArea.setText("dividing by zero");
        }
    }
}