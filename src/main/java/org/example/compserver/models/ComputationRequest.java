package org.example.compserver.models;

import org.example.compserver.models.exceptions.InvalidVariableValuesFunctionException;
import org.example.compserver.models.exceptions.MalformedRequestException;
import org.example.compserver.models.expressions.Expression;
import org.example.compserver.models.expressions.exceptions.InvalidExpressionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
