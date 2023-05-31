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

        Expression e = new Expression("((x0+(2.0^x1))/(21.1-x0))");
        Map<String, Double> varVal = new HashMap<>();
        varVal.put("x0", 1.0);
        varVal.put("x1", 20.0);
        double result = e.computeUsing(varVal);
        System.out.println(e);
        System.out.println("Result " + result);

        String s1 = "x:1:1:4,y:5:1:7,z:8:1:9";
        VariableValuesFunction vvf = new VariableValuesFunction(s1);
        System.out.println("x = " + Arrays.toString(vvf.getValuesOf("x")));
        System.out.println("y = " + Arrays.toString(vvf.getValuesOf("y")));
        System.out.println("z = " + Arrays.toString(vvf.getValuesOf("z")));

//        List<Map<String, Double>> valueTuplesList = vvf.getValueTuplesList();
//        System.out.println(valueTuplesList);
        List<Map<String, Double>> valueTuplesGrid = vvf.getValueTuplesGrid();
        System.out.println(valueTuplesGrid);
        System.out.println(valueTuplesGrid.size());
    }
}