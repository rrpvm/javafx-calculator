module com.rrpvm.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;


    opens com.rrpvm.calculator to javafx.fxml;
    exports com.rrpvm.calculator;
    exports com.rrpvm.calculator.controller;
    exports com.rrpvm.calculator.service;
    opens com.rrpvm.calculator.controller to javafx.fxml;
}