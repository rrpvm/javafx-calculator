package com.rrpvm.calculator.pojo;

import com.rrpvm.calculator.model.MathOperandPriority;

import java.util.EmptyStackException;
import java.util.Stack;


public class MathCalculator {
    private final RPNParser parser;

    public MathCalculator() {
        this.parser = new RPNParser();
    }

    public double singleOperand(double value, String operation) throws ArithmeticException {
        if (operation.equals("1/x")) {
            if (value == 0) {
                throw new ArithmeticException("dividing by zero");
            }
            return 1.0 / value;
        } else if (operation.equals("√")) {
            if (value > 0) return Math.sqrt(value);
            throw new ArithmeticException("negative root");
        }
        return value;
    }

    public double calculate(String expression) throws ArithmeticException {
        expression = expression.trim();
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
                double right = 0;
                char operation = 'c';
                try {
                    right = Double.parseDouble(numbers.pop());
                    double left = Double.parseDouble(numbers.pop());
                    operation = operations.pop();
                    numbers.push(Double.toString(doCalculation(left, right, operation)));
                } catch (EmptyStackException e) {//minus or plus
                    if (operation == '-')
                        numbers.push(Double.toString(-right));
                    else numbers.push(Double.toString(right));
                }
            }
        }
        if (numbers.isEmpty()) throw new ArithmeticException();
        return Double.parseDouble(numbers.peek());
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
}
