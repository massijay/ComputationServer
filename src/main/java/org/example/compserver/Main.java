package org.example.compserver;

import org.example.compserver.models.ComputationServer;

public class Main {
    public static void main(String[] args) throws Exception {
        //TODO ALL EQUALS AND HASHCODE
        System.out.println("Hello world!");

//        Expression e = new Expression("(((x0+(2.0^x1))/(21.1-x0))+1)");
//        Map<String, Double> varVal = new HashMap<>();
//        varVal.put("x0", 1.0);
//        varVal.put("x1", 20.0);
//        varVal.put("y", 2000.0);
////        double result = e.computeUsing(varVal);
////        System.out.println(e);
////        System.out.println("Result " + result);
//
//        String s1 = "x0:1:1:3,x1:5:1:7,z:8:1:9,y:10:1:11";
//        VariableValuesFunction vvf = new VariableValuesFunction(s1);
//        System.out.println("x0 = " + Arrays.toString(vvf.getValuesOf("x0")));
//        System.out.println("x1 = " + Arrays.toString(vvf.getValuesOf("x1")));
//        System.out.println("z = " + Arrays.toString(vvf.getValuesOf("z")));
//        System.out.println("y = " + Arrays.toString(vvf.getValuesOf("y")));
//
//        System.out.println(vvf.getValueTuplesCount(VariableValuesFunction.ValuesKind.GRID));
//
////        List<Map<String, Double>> valueTuplesList = vvf.getValueTuplesList();
////        System.out.println(valueTuplesList);
//        List<Map<String, Double>> valueTuplesGrid = vvf.getValueTuplesOfVariables(VariableValuesFunction.ValuesKind.GRID, e.getVariables());
//        System.out.println(valueTuplesGrid);
//        System.out.println(valueTuplesGrid.size());
//        List<Map<String, Double>> allValueTuplesGrid = vvf.getAllValueTuples(VariableValuesFunction.ValuesKind.GRID);
//        System.out.println(allValueTuplesGrid);
//        System.out.println(allValueTuplesGrid.size());
//
//        String req = "MAX_GRID;x0:-1:0.1:1,x1:-10:1:20;((x0+(2.0^x1))/(21.1-x0));(x1*x0)";
//        ComputationRequest cr = ComputationRequest.fromString(req);
//        Future<Double> future = ComputationService.submit(cr);
//        System.out.println(Response.buildOk(5432100000L/1e6, future.get()));

        ComputationServer server = new ComputationServer(10000);
        server.start();

    }
}