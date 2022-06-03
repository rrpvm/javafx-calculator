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

public class MainController {
    private CalculatorService calculatorService = CalculatorService.getInstance();
    private LoggerUtil loggerUtil = LoggerUtil.getInstance();

    private String tmpValue = new String();
    @FXML
    private TextField expressionArea;

    @FXML
    private void onInput(ActionEvent event) {
        String appendChar = ((Button) event.getSource()).getText();
        final String oldAreaText = expressionArea.getText();
        if (appendChar.matches("[-+*/^]")) {
            if (oldAreaText.isEmpty()) return;
            if (oldAreaText.substring(oldAreaText.length() - 1).matches("[-+*/^]")) return;
            tmpValue = new String();
        }
        int diff = oldAreaText.length() - tmpValue.length();
        tmpValue += appendChar;
        expressionArea.setText(oldAreaText.substring(0, diff) + tmpValue);

    }

    @FXML
    private void negateValue(MouseEvent event) {
        final String oldAreaText = expressionArea.getText();
        int diff = oldAreaText.length() - tmpValue.length();
        if (tmpValue.charAt(0) == '-') {
            tmpValue = tmpValue.replace('-', '+');
        } else if (tmpValue.charAt(0) == '+') {
            tmpValue = tmpValue.replace('+', '-');
        } else if (tmpValue.substring(0, 1).matches("[*/^]")) {
            tmpValue = tmpValue.substring(0, 1) + "-" + tmpValue.substring(1);
            expressionArea.setText(oldAreaText.substring(0, diff) + tmpValue);
            return;
        } else tmpValue = "-" + tmpValue;
        expressionArea.setText(oldAreaText.substring(0, diff) + tmpValue);
        return;
    }

    @FXML
    private void onErase(MouseEvent mouseEvent) {
        if (expressionArea.getText().isEmpty()) return;
        expressionArea.setText(expressionArea.getText(0, expressionArea.getText().length() - 1));
        tmpValue = tmpValue.substring(0, tmpValue.length() - 1);
    }

    @FXML
    private void onClear(MouseEvent mouseEvent) {
        expressionArea.clear();
        tmpValue = new String();
    }

    @FXML
    private void onCalculateClicked(MouseEvent mouseEvent) {
        final String oldExpressionArea = expressionArea.getText();
        if (oldExpressionArea.isEmpty()) return;
        if (oldExpressionArea.substring(oldExpressionArea.length() - 1).matches("[-+*/^]"))
            return;
        String result = oldExpressionArea;
        try {
            result = Double.toString(calculatorService.calculate(oldExpressionArea));
        } catch (ArithmeticException arithmeticException) {
            result = Double.toString(Double.POSITIVE_INFINITY);
        } finally {
            expressionArea.setText(result);
            loggerUtil.wakeLogger(new RequestLog(oldExpressionArea, result, new Timestamp(java.util.Date.from(Instant.now()).getTime())));
            tmpValue = expressionArea.getText();
        }
    }

    @FXML
    private void singleCalculation(ActionEvent actionEvent) {
        final String oldExpressionArea = expressionArea.getText();
        if (oldExpressionArea.isEmpty()) return;
        if (oldExpressionArea.substring(oldExpressionArea.length() - 1).matches("[-+*/^]"))
            return;
        double singleValue = 0.0;
        String result = oldExpressionArea;
        try {
            singleValue = Double.parseDouble(oldExpressionArea);
            result = Double.toString(this.calculatorService.singleOperationCalculate(singleValue, ((Button) actionEvent.getSource()).getText()));
        } catch (NumberFormatException numberFormatException) {
            try {
                singleValue = this.calculatorService.calculate(oldExpressionArea);
                result = Double.toString(this.calculatorService.singleOperationCalculate(singleValue, ((Button) actionEvent.getSource()).getText()));
            } catch (ArithmeticException e) {
                result = e.getMessage();
            }
        } finally {
            expressionArea.setText(result);
            loggerUtil.wakeLogger(new RequestLog(oldExpressionArea, result, new Timestamp(java.util.Date.from(Instant.now()).getTime())));
            tmpValue = expressionArea.getText();
        }
    }
}