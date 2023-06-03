package org.example.compserver.models;


import org.example.compserver.models.exceptions.ValueTuplesGenerationException;
import org.example.compserver.models.expressions.Expression;
import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Computer {
    private Computer() {
    }

    public static double compute(ComputationRequest request) throws ValueTuplesGenerationException, VariableNotDefinedException {
        Objects.requireNonNull(request);
        ComputationRequest.ComputationKind computationKind = request.getComputationKind();
        VariableValuesFunction variableValuesFunction = request.getVariableValuesFunction();
        VariableValuesFunction.ValuesKind valuesKind = request.getValuesKind();
        List<Expression> expressions = request.getExpressions();

        if (computationKind == ComputationRequest.ComputationKind.COUNT) {
            return variableValuesFunction.getValueTuplesCount(valuesKind);
        }

        if (computationKind == ComputationRequest.ComputationKind.AVG) {
            expressions = List.of(expressions.get(0));
        }

        List<Double> results = new ArrayList<>();
        for (Expression e : expressions) {
            List<Map<String, Double>> valueTuples = variableValuesFunction.getValueTuplesOfVariables(valuesKind, e.getVariables());
            for (Map<String, Double> valueTuple : valueTuples) {
                results.add(e.computeUsing(valueTuple));
            }
        }

        if (computationKind == ComputationRequest.ComputationKind.AVG) {
            return results.stream().mapToDouble(d -> d)
                    .average().orElseThrow();
        }
        if (computationKind == ComputationRequest.ComputationKind.MAX) {
            return results.stream().mapToDouble(x -> x)
                    .max().orElseThrow();
        }
        return results.stream().mapToDouble(x -> x)
                .min().orElseThrow();
    }
}
