package com.rrpvm.calculator.pojo;

import com.rrpvm.calculator.model.MathOperandPriority;

import java.util.EmptyStackException;
import java.util.Stack;


public class MathCalculator {
    private RPNParser parser;

    public MathCalculator() {
        this.parser = new RPNParser();
    }

    private double doCalculation(double a, double b, char operand) throws ArithmeticException {
        double result = 0.0;
        switch (operand) {
            case '+': {
                result = a + b;
                break;
            }
            case '-': {
                result = a - b;
                break;
            }
            case '*': {
                result = a * b;
                break;
            }
            case '/': {
                if (b == 0.0) {
                    throw new ArithmeticException("diving by zero");
                }
                result = a / b;
                break;
            }
            case '^': {
                result = Math.pow(a, b);
                break;
            }
            default: {
                break;
            }
        }
        return result;
    }

    public double calculate(String expression) throws ArithmeticException, IllegalArgumentException {
        expression = expression.trim();
        if (!parser.expressionValidation(expression)) {
            throw new IllegalArgumentException();
        }
        String rnpExpression = parser.generateRNP(expression);
        Stack<Character> operations = new Stack<>();
        Stack<String> numbers = new Stack<>();
        String tmp = "";
        for (int i = 0; i < rnpExpression.length(); i++) {
            char currentChar = rnpExpression.charAt(i);
            int priority = parser.getCharacterPriority(currentChar);
            if (priority == MathOperandPriority.NUMBER.getPriority()) tmp += currentChar;
            if (currentChar == ' ' || priority != MathOperandPriority.NUMBER.getPriority()) {
                tmp = tmp.trim();
                if (!tmp.isEmpty()) {
                    numbers.push(tmp);
                    tmp = "";
                }
            }
            if (priority >= MathOperandPriority.PLUS_MINUS.getPriority()) {
                operations.push(currentChar);
            }
            if (!operations.isEmpty()) {
                try {
                    double right = Double.parseDouble(numbers.pop());
                    double left = Double.parseDouble(numbers.pop());
                    char operation = operations.pop();
                    numbers.push(Double.toString(doCalculation(left, right, operation)));
                } catch (EmptyStackException e) {
                    /*унарная операция - корень и тд*/
                }
            }
        }
        if (numbers.isEmpty()) throw new ArithmeticException();
        return Double.parseDouble(numbers.peek());
    }
}
