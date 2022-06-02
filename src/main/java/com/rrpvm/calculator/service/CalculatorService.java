package com.rrpvm.calculator.service;

import com.rrpvm.calculator.pojo.MathCalculator;


public class CalculatorService {
    private static CalculatorService instance;
    private MathCalculator calculator;

    private CalculatorService() {//only singleton
        calculator = new MathCalculator();
    }

    public static CalculatorService getInstance() {
        if (instance == null) {
            instance = new CalculatorService();
        }
        return instance;
    }

    public double calculate(String expression) throws IllegalArgumentException, ArithmeticException {
        return this.calculator.calculate(expression);
    }

    public double singleOperationCalculate(String input, String operation) throws ArithmeticException {
        if (input.isEmpty()) throw new IllegalArgumentException("input is null");
        return this.calculator.singleOperand(Double.parseDouble(input.trim()), operation);
    }
}
