package com.rrpvm.calculator.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainController {
    @FXML
    private TextField expressionArea;
    @FXML
    public void onInput(ActionEvent event){
        String txt = ((Button)event.getSource()).getText();
        expressionArea.setText(expressionArea.getText() + txt);

    }
}