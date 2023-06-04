package org.example.compserver.models;

import org.example.compserver.models.exceptions.ValueTuplesGenerationException;
import org.example.compserver.models.expressions.Expression;
import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ComputationService {
    private static final int CORES_COUNT = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService service = Executors.newFixedThreadPool(CORES_COUNT);

    private ComputationService() {
    }

    public static Future<Double> submit(ComputationRequest request) {
        return service.submit(() -> compute(request));
    }

    private static double compute(ComputationRequest request) throws ValueTuplesGenerationException, VariableNotDefinedException {
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
            return results.stream().mapToDouble(Double::doubleValue)
                    .average().orElseThrow();
        }
        if (computationKind == ComputationRequest.ComputationKind.MAX) {
            return results.stream().mapToDouble(Double::doubleValue)
                    .max().orElseThrow();
        }
        return results.stream().mapToDouble(Double::doubleValue)
                .min().orElseThrow();
    }
}
