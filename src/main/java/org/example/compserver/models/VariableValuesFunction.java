package org.example.compserver.models;

import org.example.compserver.models.exceptions.InvalidVariableValuesFunctionException;
import org.example.compserver.models.exceptions.ValueTuplesGenerationException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VariableValuesFunction implements Function<String, double[]> {
    private final Map<String, double[]> variablesMap;

    public VariableValuesFunction(String string) throws InvalidVariableValuesFunctionException {
        String[] variablesValues = string.split(",");
        variablesMap = new HashMap<>(variablesValues.length);
        parse(variablesValues);
    }

    private void parse(String[] variablesValues) throws InvalidVariableValuesFunctionException {
        for (String vv : variablesValues) {
            String[] splitted = vv.split(":");
            if (splitted.length != 4) {
                throw new InvalidVariableValuesFunctionException(vv,
                        "4 colon-separated parameters expected, got " + splitted.length);
            }
            String name = splitted[0];
            double[] parsed = new double[3];
            for (int i = 1; i < splitted.length; i++) {
                try {
                    parsed[i - 1] = Double.parseDouble(splitted[i]);
                } catch (NumberFormatException e) {
                    throw new InvalidVariableValuesFunctionException(vv, "Invalid number \"" + splitted[i] + "\"", e);
                }
            }
            if (parsed[1] <= 0) {
                throw new InvalidVariableValuesFunctionException(vv, "Step cannot be <=0, got " + parsed[1]);
            }
            if (parsed[0] > parsed[2]) {
                throw new InvalidVariableValuesFunctionException(vv,
                        "Lower range value (" + parsed[0] + ") cannot be greater than the upper (" + parsed[2] + ")");
            }
            int n = (int) Math.floor((parsed[2] - parsed[0]) / parsed[1]) + 1;
            double[] values = new double[n];
            for (int i = 0; i < n; i++) {
                values[i] = parsed[0] + i * parsed[1];
            }
            variablesMap.put(name, values);
        }
    }

    public double[] getValuesOf(String variableName) {
        if (variablesMap.containsKey(variableName)) {
            return variablesMap.get(variableName);
        }
        return new double[0];
    }

    public int getValueTuplesCount(ValuesKind valuesKind) throws ValueTuplesGenerationException {
        if (valuesKind == ValuesKind.LIST) {
            if (hasValuesWithDifferentLength(variablesMap)) {
                throw new ValueTuplesGenerationException("Provided variables have different length value tuples");
            }
            return variablesMap.entrySet().iterator().next().getValue().length;
        }
        // ValuesKind.GRID
        return variablesMap.values().stream()
                .map(arr -> arr.length)
                .reduce(1, (a, b) -> a * b);
    }


    public List<Map<String, Double>> getAllValueTuples(ValuesKind valuesKind) throws ValueTuplesGenerationException {
        return getValueTuplesOfVariables(valuesKind, null);
    }

    public List<Map<String, Double>> getValueTuplesOfVariables(ValuesKind valuesKind, Set<String> variables) throws ValueTuplesGenerationException {
        Map<String, double[]> map;
        if (variables == null) {
            map = this.variablesMap;
        } else {
            if (!variablesMap.keySet().containsAll(variables)) {
                throw new ValueTuplesGenerationException("Passed variables must be a subset (or the same set) of the variables mapped in this VariableValuesFunction");
            }
            map = this.variablesMap.entrySet().stream()
                    .filter(kv -> variables.contains(kv.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return switch (valuesKind) {
            case LIST -> getValueTuplesList(map);
            case GRID -> getValueTuplesGrid(map);
        };
    }

    private static List<Map<String, Double>> getValueTuplesList(Map<String, double[]> variablesMap) throws ValueTuplesGenerationException {
        if (hasValuesWithDifferentLength(variablesMap)) {
            throw new ValueTuplesGenerationException("Provided variables have different length value tuples");
        }

        int length = variablesMap.values().iterator().next().length;
        List<Map<String, Double>> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            Map<String, Double> m = new HashMap<>(variablesMap.keySet().size());
            for (String varName : variablesMap.keySet()) {
                m.put(varName, variablesMap.get(varName)[i]);
            }
            list.add(m);
        }
        return list;
    }

    private static boolean hasValuesWithDifferentLength(Map<String, double[]> variablesMap) {
        return variablesMap.values().stream()
                .mapToInt(values -> values.length)
                .distinct()
                .count() != 1;
    }

    private static List<Map<String, Double>> getValueTuplesGrid(Map<String, double[]> variablesMap) {
        List<Map<String, Double>> list = null;
        for (Map.Entry<String, double[]> variableValues : variablesMap.entrySet()) {
            list = getNewValueTuplesGridWith(list, variableValues);
        }
        return list;
    }

    private static List<Map<String, Double>> getNewValueTuplesGridWith(List<Map<String, Double>> oldGrid, Map.Entry<String, double[]> variableValues) {
        if (oldGrid == null || oldGrid.size() == 0) {
            Map<String, Double> emptyMap = new HashMap<>(0);
            oldGrid = new ArrayList<>(1);
            oldGrid.add(emptyMap);
        }
        List<Map<String, Double>> newList = new ArrayList<>(oldGrid.size() * variableValues.getValue().length);
        for (Map<String, Double> oldMap : oldGrid) {
            for (double d : variableValues.getValue()) {
                Map<String, Double> m = new HashMap<>(oldMap.size() + 1);
                m.putAll(oldMap);
                m.put(variableValues.getKey(), d);
                newList.add(m);
            }
        }
        return newList;
    }

    @Override
    public double[] apply(String s) {
        return getValuesOf(s);
    }

    public enum ValuesKind {
        GRID, LIST
    }
}
