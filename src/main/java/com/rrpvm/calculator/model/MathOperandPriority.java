package com.rrpvm.calculator.model;

public enum MathOperandPriority {
    NUMBER(0), PLUS_MINUS(1), MULTIPLY_DIVIDE(2);
    private int priority;

    public int getPriority() {
        return priority;
    }

    MathOperandPriority(int priority) {
        this.priority = priority;
    }
}
