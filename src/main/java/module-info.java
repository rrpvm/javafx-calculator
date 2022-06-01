module com.rrpvm.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;
    requires java.sql;


    opens com.rrpvm.calculator to javafx.fxml;
    exports com.rrpvm.calculator;
    exports com.rrpvm.calculator.controller;
    exports com.rrpvm.calculator.service;
    exports com.rrpvm.calculator.pojo;
    opens com.rrpvm.calculator.controller to javafx.fxml;
    exports com.rrpvm.calculator.pojo.di.interfaces;
}