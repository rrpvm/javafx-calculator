package com.rrpvm.calculator.controller;

import com.rrpvm.calculator.service.CalculatorService;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;

public class MainController {
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
        if (this.expressionArea.getText().isEmpty()) {
            //throw new exception
            return;
        }
        CalculatorService calculatorService = CalculatorService.getInstance();
        try {
            double result = calculatorService.calculate(this.expressionArea.getText());
            this.expressionArea.setText(Double.toString(result));
        } catch (NumberFormatException n) {
            n.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            //this.expressionArea.setText("something goes wrong");
        }
    }
}