module com.rrpvm.calculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rrpvm.calculator to javafx.fxml;
    exports com.rrpvm.calculator;
    exports com.rrpvm.calculator.controller;
    opens com.rrpvm.calculator.controller to javafx.fxml;
}