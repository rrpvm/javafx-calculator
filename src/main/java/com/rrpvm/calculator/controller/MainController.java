package com.rrpvm.calculator.controller;

import com.rrpvm.calculator.model.RequestLog;
import com.rrpvm.calculator.pojo.DataBaseReader;
import com.rrpvm.calculator.pojo.connectors.PostgresDataBaseConnector;
import com.rrpvm.calculator.service.CalculatorService;
import com.rrpvm.calculator.service.IpResolverService;
import com.rrpvm.calculator.utils.LoggerUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.util.Pair;

import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private CalculatorService calculatorService = CalculatorService.getInstance();
    private LoggerUtil loggerUtil = LoggerUtil.getInstance();
    private DataBaseReader dataBaseReader = new DataBaseReader(new PostgresDataBaseConnector(), new IpResolverService());

    private String tmpValue = new String();
    private LinkedList<Pair<String, String>> requestsHistory = new LinkedList<>();
    @FXML
    private TextField expressionArea;
    @FXML
    private ListView historyListView;

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
        if (oldAreaText.isEmpty()) return;
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
        if (tmpValue.isEmpty()) return;
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
            this.requestsHistory.removeLast();
            this.requestsHistory.addFirst(new Pair<>(oldExpressionArea, result));
            this.historyListView.setItems(FXCollections.observableList(this.requestsHistory));
            expressionArea.setText(result);
            loggerUtil.wakeLogger(new RequestLog(oldExpressionArea, result, new Timestamp(java.util.Date.from(Instant.now()).getTime())));
            tmpValue = expressionArea.getText();

        }
    }

    @FXML
    private void singleCalculation(ActionEvent actionEvent) {
        final String oldExpressionArea = expressionArea.getText();
        final String unaryOperation = ((Button) actionEvent.getSource()).getText();
        if (oldExpressionArea == null || oldExpressionArea.isEmpty()) return;
        if (oldExpressionArea.substring(oldExpressionArea.length() - 1).matches("[-+*/^]"))
            return;
        double singleValue = 0.0;
        String result = oldExpressionArea;
        try {
            singleValue = Double.parseDouble(oldExpressionArea);
            result = Double.toString(this.calculatorService.singleOperationCalculate(singleValue, unaryOperation));
        } catch (NumberFormatException numberFormatException) {
            try {
                singleValue = this.calculatorService.calculate(oldExpressionArea);
                result = Double.toString(this.calculatorService.singleOperationCalculate(singleValue, unaryOperation));
            } catch (ArithmeticException e) {
                result = e.getMessage();
            }
        } finally {
            this.requestsHistory.removeLast();
            this.requestsHistory.addFirst(new Pair<>(unaryOperation + oldExpressionArea, result));
            this.historyListView.setItems(FXCollections.observableList(this.requestsHistory));
            expressionArea.setText(result);
            loggerUtil.wakeLogger(new RequestLog(unaryOperation + oldExpressionArea, result, new Timestamp(java.util.Date.from(Instant.now()).getTime())));
            tmpValue = expressionArea.getText();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataBaseReader.setup();
        this.requestsHistory = (LinkedList<Pair<String, String>>) dataBaseReader.readClientLogs();
        this.historyListView.setItems(FXCollections.observableList(this.requestsHistory));
        dataBaseReader.release();
    }
}