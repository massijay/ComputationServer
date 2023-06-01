package org.example.compserver;

import org.example.compserver.models.VariableValuesFunction;
import org.example.compserver.models.expressions.Expression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");

        Expression e = new Expression("(((x0+(2.0^x1))/(21.1-x0))+y)");
        Map<String, Double> varVal = new HashMap<>();
        varVal.put("x0", 1.0);
        varVal.put("x1", 20.0);
        varVal.put("y", 2000.0);
        double result = e.computeUsing(varVal);
        System.out.println(e);
        System.out.println("Result " + result);

        String s1 = "x0:1:1:4,x1:5:1:7,z:8:1:9,y:10:1:11";
        VariableValuesFunction vvf = new VariableValuesFunction(s1);
        System.out.println("x0 = " + Arrays.toString(vvf.getValuesOf("x0")));
        System.out.println("x1 = " + Arrays.toString(vvf.getValuesOf("x1")));
        System.out.println("z = " + Arrays.toString(vvf.getValuesOf("z")));
        System.out.println("w = " + Arrays.toString(vvf.getValuesOf("y")));

//        List<Map<String, Double>> valueTuplesList = vvf.getValueTuplesList();
//        System.out.println(valueTuplesList);
        List<Map<String, Double>> valueTuplesGrid = vvf.getValueTuplesOfVariables(VariableValuesFunction.ValuesKind.GRID, e.getVariables());
        System.out.println(valueTuplesGrid);
        System.out.println(valueTuplesGrid.size());
        List<Map<String, Double>> allValueTuplesGrid = vvf.getAllValueTuples(VariableValuesFunction.ValuesKind.GRID);
        System.out.println(allValueTuplesGrid);
        System.out.println(allValueTuplesGrid.size());
    }
}