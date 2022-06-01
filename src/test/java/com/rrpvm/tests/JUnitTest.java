package com.rrpvm.tests;

import com.rrpvm.calculator.pojo.RPNParser;
import com.rrpvm.calculator.service.CalculatorService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JUnitTest {
    @Test
    public void orderTest() {
        CalculatorService calculatorService = CalculatorService.getInstance();
        Assert.assertEquals(calculatorService.calculate("2+2*2"), 6.0);
        Assert.assertEquals(calculatorService.calculate("2*2+2"), 6.0);
        Assert.assertEquals(calculatorService.calculate("1+2*3+4"), 11.0);
        Assert.assertEquals(calculatorService.calculate("432-43213*5/2"), -107600.5);
        Assert.assertEquals(calculatorService.calculate("54534*33+43242-31231231-0.1/4"), -29388367.025);

    }

    @Test
    public void validTest() {
        RPNParser parser = new RPNParser();
        Assert.assertNotEquals(parser.expressionValidation("-"), true);
        Assert.assertNotEquals(parser.expressionValidation("+"), true);
        Assert.assertNotEquals(parser.expressionValidation("*"), true);
        Assert.assertNotEquals(parser.expressionValidation("/"), true);
        Assert.assertNotEquals(parser.expressionValidation(""), true);
        Assert.assertNotEquals(parser.expressionValidation(" "), true);
        Assert.assertNotEquals(parser.expressionValidation("    "), true);
    }
    @Test(expectedExceptions = NumberFormatException.class)
    public void parsingExceptionTest() {
        CalculatorService calculatorService = CalculatorService.getInstance();
        calculatorService.calculate("432fgdgfdg+31");
        calculatorService.calculate("-");
    }
}
