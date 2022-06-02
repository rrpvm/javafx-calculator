package com.rrpvm.tests;

import com.rrpvm.calculator.pojo.RPNParser;
import com.rrpvm.calculator.service.CalculatorService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JUnitTest {
    @Test
    public void orderTest() {
        CalculatorService calculatorService = CalculatorService.getInstance();
        Assert.assertEquals(calculatorService.calculate("0/1"), 0.0);
        Assert.assertEquals(calculatorService.calculate("1^0"), 1.0);
        Assert.assertEquals(calculatorService.calculate("5^0"), 1.0);
        Assert.assertEquals(calculatorService.calculate("0^5"), 0.0);
        Assert.assertEquals(calculatorService.calculate("1+1-2*2"), -2.0);
        Assert.assertEquals(calculatorService.calculate("2*2+2*2"), 8.0);
        Assert.assertEquals(calculatorService.calculate("33/33*2^5"), 32.0);
        Assert.assertEquals(calculatorService.calculate("432-43213*5/2"), -107600.5);
        Assert.assertEquals(calculatorService.calculate("54534*33+43242-31231231-0.1/4"), -29388367.025);
    }

    @Test
    public void validTest() {
        RPNParser parser = new RPNParser();
        Assert.assertEquals(parser.expressionValidation(""), false);
        Assert.assertEquals(parser.expressionValidation(" "), false);
        Assert.assertEquals(parser.expressionValidation("+"), false);
        Assert.assertEquals(parser.expressionValidation("-"), false);
        Assert.assertEquals(parser.expressionValidation("*"), false);
        Assert.assertEquals(parser.expressionValidation("/"), false);
        Assert.assertEquals(parser.expressionValidation("^"), false);
        Assert.assertEquals(parser.expressionValidation("1+"), false);
        Assert.assertEquals(parser.expressionValidation("1*"), false);
        Assert.assertEquals(parser.expressionValidation("1/"), false);
        Assert.assertEquals(parser.expressionValidation("1^"), false);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void parsingExceptionTest() {
        CalculatorService calculatorService = CalculatorService.getInstance();
        calculatorService.calculate("-");
        calculatorService.calculate("+-");
        calculatorService.calculate("432fgdgfdg+31");
    }

    @Test(expectedExceptions = ArithmeticException.class)
    public void calculatingExceptions() {
        CalculatorService calculatorService = CalculatorService.getInstance();
        calculatorService.calculate("0/0");
    }
}
