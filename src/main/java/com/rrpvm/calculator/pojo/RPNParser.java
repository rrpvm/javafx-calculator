package com.rrpvm.calculator.pojo;

import com.rrpvm.calculator.model.MathOperandPriority;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RPNParser {
    public boolean expressionValidation(String originalExpression) {
        if (originalExpression.isEmpty()) return false;
        Pattern pattern = Pattern.compile("[0-9]+");
        Pattern pattern2 = Pattern.compile("[-+*/^]+");
        Matcher matcher = pattern.matcher(originalExpression);
        Matcher matcher2 = pattern2.matcher(originalExpression);
        if (!matcher.find()) return false;//нет чисел
        if (!matcher2.find()) return false;//нет знаков
        return true;
    }

    public int getCharacterPriority(char c) {
        if (c == '^') return MathOperandPriority.POW_OPERAND.getPriority();
        else if (c == '+' || c == '-') return MathOperandPriority.PLUS_MINUS.getPriority();
        else if (c == '*' || c == '/') return MathOperandPriority.MULTIPLY_DIVIDE.getPriority();
        return MathOperandPriority.NUMBER.getPriority();
    }

    public String generateRNP(String expression) {
        String rnp = "";
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < expression.length(); ++i) {
            int priority = getCharacterPriority(expression.charAt(i));
            if (priority == MathOperandPriority.NUMBER.getPriority()) rnp += expression.charAt(i);
            if (priority > MathOperandPriority.NUMBER.getPriority()) {
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
}
