package org.example.compserver.models;

import org.example.compserver.models.exceptions.InvalidVariableValuesFunctionException;
import org.example.compserver.models.exceptions.MalformedRequestException;
import org.example.compserver.models.exceptions.ValueTuplesGenerationException;
import org.example.compserver.models.expressions.Expression;
import org.example.compserver.models.expressions.exceptions.InvalidExpressionException;
import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.*;

public class ComputationRequest implements Request {
    private final ComputationKind computationKind;
    private final VariableValuesFunction.ValuesKind valuesKind;
    private final VariableValuesFunction variableValuesFunction;
    private final List<Expression> expressions;

    private ComputationRequest(ComputationKind computationKind,
                               VariableValuesFunction.ValuesKind valuesKind,
                               VariableValuesFunction variableValuesFunction,
                               List<Expression> expressions) {

        this.computationKind = computationKind;
        this.valuesKind = valuesKind;
        this.variableValuesFunction = variableValuesFunction;
        this.expressions = expressions;
    }

    public static ComputationRequest fromString(String requestString) throws MalformedRequestException {
        String[] split = requestString.split("_");
        if (split.length != 2) {
            return null;
        }
        ComputationKind computationKind;
        try {
            computationKind = ComputationKind.valueOf(split[0]);
        } catch (IllegalArgumentException e) {
            return null;
        }
        String[] data = split[1].split(";");
        if (data.length < 3) {
            throw new MalformedRequestException(requestString,
                    "Invalid ComputationRequest, expected at least 3 semicolon-separated parameters after ComputationKind, got " + data.length +
                            "\nReceived request: " + requestString);
        }
        VariableValuesFunction.ValuesKind valuesKind;
        try {
            valuesKind = VariableValuesFunction.ValuesKind.valueOf(data[0]);
        } catch (IllegalArgumentException e) {
            throw new MalformedRequestException(requestString,
                    "Invalid ValuesKind received, expected one of " +
                            Arrays.toString(VariableValuesFunction.ValuesKind.values()) +
                            ", got " + data[0],
                    e);
        }
        VariableValuesFunction vvf;
        try {
            vvf = new VariableValuesFunction(data[1]);
        } catch (InvalidVariableValuesFunctionException e) {
            throw new MalformedRequestException(requestString,
                    "Invalid VariableValuesFunction received\n" + e.getMessage()
                    , e);
        }
        List<Expression> expressions = new ArrayList<>(data.length - 2);
        for (int i = 2; i < data.length; i++) {
            try {
                expressions.add(new Expression(data[i]));
            } catch (InvalidExpressionException e) {
                throw new MalformedRequestException(requestString, e.getMessage(), e);
            }
        }
        return new ComputationRequest(computationKind, valuesKind, vvf, expressions);
    }

    public String compute() throws ValueTuplesGenerationException, VariableNotDefinedException {
        if (computationKind == ComputationKind.COUNT) {
            int count = variableValuesFunction.getValueTuplesCount(valuesKind);
            return Integer.toString(count);
        }

        if (computationKind == ComputationKind.AVG) {
            Expression e = expressions.get(0);
            List<Map<String, Double>> valueTuples = variableValuesFunction.getValueTuplesOfVariables(valuesKind, e.getVariables());
            List<Double> res = new ArrayList<>(valueTuples.size());//verificare
            for (Map<String, Double> valueTuple : valueTuples) {
                res.add(e.computeUsing(valueTuple));
            }
            double avg = res.stream()
                    .mapToDouble(d -> d)
                    .average().orElseThrow();
            return Double.toString(avg);
        }

        List<Double> results = new ArrayList<>();
        for (Expression e : expressions) {
            List<Map<String, Double>> valueTuples = variableValuesFunction.getValueTuplesOfVariables(valuesKind, e.getVariables());
            for (Map<String, Double> valueTuple : valueTuples) {
                results.add(e.computeUsing(valueTuple));
            }
        }
        if (computationKind == ComputationKind.MAX) {
            double max = results.stream().mapToDouble(x -> x)
                    .max().orElseThrow();
            return Double.toString(max);
        }
        double min = results.stream().mapToDouble(x -> x)
                .min().orElseThrow();
        return Double.toString(min);
    }

    public ComputationKind getComputationKind() {
        return computationKind;
    }

    public VariableValuesFunction.ValuesKind getValuesKind() {
        return valuesKind;
    }

    public VariableValuesFunction getVariableValuesFunction() {
        return variableValuesFunction;
    }

    public List<Expression> getExpressions() {
        return Collections.unmodifiableList(expressions);
    }

    public enum ComputationKind {
        MIN, MAX, AVG, COUNT
    }
}
