package org.example.compserver;

import org.example.compserver.models.VariableValuesFunction;
import org.example.compserver.models.exceptions.InvalidVariableValuesFunction;
import org.example.compserver.models.expressions.Expression;
import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws VariableNotDefinedException, InvalidVariableValuesFunction {
        System.out.println("Hello world!");

        Expression e = new Expression("((x0+(2.0^x1))/(21.1-x0))");
        Map<String, Double> varVal = new HashMap<>();
        varVal.put("x0", 1.0);
        varVal.put("x1", 20.0);
        double result = e.computeUsing(varVal);
        System.out.println(e);
        System.out.println("Result " + result);

        String s1 = "x0:-1:11:1,x1:-10:1:20.5";
        VariableValuesFunction vvf = new VariableValuesFunction(s1);
        System.out.println("x0 = " + Arrays.toString(vvf.getValuesOf("x0")));
        System.out.println("x1 = " + Arrays.toString(vvf.getValuesOf("x1")));
        System.out.println("x2 = " + Arrays.toString(vvf.getValuesOf("x2")));
    }
}