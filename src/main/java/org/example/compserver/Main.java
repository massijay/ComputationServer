package org.example.compserver;

import org.example.compserver.models.expressions.Expression;
import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws VariableNotDefinedException {
        System.out.println("Hello world!");

        Expression e = new Expression("((x0+(2.0^x1))/(21.1-x0))");
        Map<String, Double> varVal = new HashMap<>();
        varVal.put("x0", 1.0);
        varVal.put("x1", 20.0);
        double result = e.computeUsing(varVal);
        System.out.println("Result " + result);
    }
}