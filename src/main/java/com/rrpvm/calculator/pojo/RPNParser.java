package com.rrpvm.calculator.pojo;

import com.rrpvm.calculator.model.MathOperandPriority;

import java.util.Stack;
public class RPNParser {
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
