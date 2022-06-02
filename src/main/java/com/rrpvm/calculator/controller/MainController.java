package com.rrpvm.calculator.controller;

import com.rrpvm.calculator.model.RequestLog;
import com.rrpvm.calculator.service.CalculatorService;
import com.rrpvm.calculator.utils.LoggerUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {
    private CalculatorService calculatorService = CalculatorService.getInstance();
    private LoggerUtil loggerUtil = LoggerUtil.getInstance();
    @FXML
    private TextField expressionArea;

    @FXML
    private void onInput(ActionEvent event) {
        String txt = ((Button) event.getSource()).getText();
        expressionArea.setText(expressionArea.getText() + txt);
    }

    @FXML
    private void onErase(MouseEvent mouseEvent) {
        if (expressionArea.getText().isEmpty()) return;
        expressionArea.setText(expressionArea.getText(0, expressionArea.getText().length() - 1));
    }

    @FXML
    private void onClear(MouseEvent mouseEvent) {
        expressionArea.clear();
    }

    @FXML
    private void onCalculateClicked(MouseEvent mouseEvent) {
        try {
            double result = calculatorService.calculate(this.expressionArea.getText());
            loggerUtil.wakeLogger(new RequestLog(this.expressionArea.getText(), result, new Timestamp(java.util.Date.from(Instant.now()).getTime())));
            this.expressionArea.setText(Double.toString(result));
        } catch (IllegalArgumentException exception) {
            //вообще - подстветить поле, что нужно еще что-то
        } catch (ArithmeticException e) {
            loggerUtil.wakeLogger(new RequestLog(this.expressionArea.getText(), Double.POSITIVE_INFINITY, new Timestamp(java.util.Date.from(Instant.now()).getTime())));
            this.expressionArea.setText("dividing by zero");
        }
    }

    @FXML
    private void singleCalculation(ActionEvent actionEvent) {
        String expression = this.expressionArea.getText();
        Pattern anyBinaryOperands = Pattern.compile("[-+*/^]");
        Matcher matcher = anyBinaryOperands.matcher(expression);
        if (matcher.find()) {
            try {
                double result = calculatorService.calculate(this.expressionArea.getText());
                this.expressionArea.setText(Double.toString(result));
            } catch (IllegalArgumentException e) {
                System.out.println("illegal argument");
                return;
            } catch (ArithmeticException e) {
                System.out.println("dividing by infinity");
                return;
            }
        }
        try {
            double result = this.calculatorService.singleOperationCalculate(expressionArea.getText(), ((Button) (actionEvent.getSource())).getText());
            String logExpression = String.format("(%s)%s", expression, ((Button) (actionEvent.getSource())).getText());
            loggerUtil.wakeLogger(new RequestLog(logExpression, result, new Timestamp(java.util.Date.from(Instant.now()).getTime())));
            this.expressionArea.setText(Double.toString(result));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}