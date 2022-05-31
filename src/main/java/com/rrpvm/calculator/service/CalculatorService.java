package com.rrpvm.calculator.service;

import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorService {
    private static CalculatorService instance;

    private CalculatorService() {//only singleton

    }

    public static CalculatorService getInstance() {
        if (instance == null) {
            instance = new CalculatorService();
        }
        return instance;
    }

    private Stack<Character> parseOperations() {
        return new Stack<Character>();
    }

    private int getCharacterPriority(char c) {
        if (c == '+' || c == '-') return 2;
        else if (c == '*' || c == '/') return 3;
        return 0;
    }

    private String generateRNP(String expression) {
        String rnp = "";
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < expression.length(); ++i) {
            int priority = getCharacterPriority(expression.charAt(i));
            if (priority == 0) rnp += expression.charAt(i);
            if (priority > 1) {
                while (!stack.isEmpty()) {
                    if (getCharacterPriority(stack.peek()) >= priority) rnp += stack.pop();
                    else break;
                }
                stack.push(expression.charAt(i));
                rnp += " ";
            }
        }
        while (!stack.isEmpty()) rnp += stack.pop();
        return rnp;
    }

    public boolean expressionValidation(String orginialExpression) {
        if (orginialExpression.isEmpty()) return false;
        Pattern pattern = Pattern.compile("[0-9]+");
        Pattern pattern2 = Pattern.compile("[-+*/]+");
        Matcher matcher = pattern.matcher(orginialExpression);
        Matcher matcher2 = pattern2.matcher(orginialExpression);
        if (!matcher.find()) return false;//нет чисел
        if (!matcher2.find()) return false;//нет знаков
        return true;
    }

    public double calculate(String expression) throws NumberFormatException {
        if (!expressionValidation(expression.trim())) {
            return Double.parseDouble(expression);
        }
        String rnpExpression = generateRNP(expression);
        Stack<Character> operations = new Stack<>();
        Stack<String> numbers = new Stack<>();
        String str = "";
        for (int i = 0; i < rnpExpression.length(); i++) {
            char currentChar = rnpExpression.charAt(i);
            int priority = getCharacterPriority(currentChar);
            if (priority == 0) {
                str += currentChar;
            }
            if (currentChar == ' ' || priority != 0) {
                str = str.trim();
                if (!str.isEmpty()) {
                    numbers.push(str);
                    str = "";
                }
            }
            if (priority > 1) {
                operations.push(currentChar);
            }
            if (!operations.isEmpty()) {
                double right = Double.parseDouble(numbers.pop());
                double left = Double.parseDouble(numbers.pop());
                char operation = operations.pop();
                if (operation == '+') {
                    numbers.push(Double.toString(left + right));
                } else if (operation == '-') {
                    numbers.push(Double.toString(left - right));
                } else if (operation == '/') {
                    if (right == 0 || right == 0.0) {
                        throw new IllegalArgumentException("diving by zero");
                    }
                    numbers.push(Double.toString(left / right));
                } else if (operation == '*') {
                    numbers.push(Double.toString(left * right));
                }
            }
        }

        return Double.parseDouble(numbers.peek());
    }
}
