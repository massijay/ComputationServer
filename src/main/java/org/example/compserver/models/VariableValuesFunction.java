package org.example.compserver.models;

import org.example.compserver.models.exceptions.InvalidVariableValuesFunction;
import org.example.compserver.models.exceptions.ValueTuplesGenerationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class VariableValuesFunction implements Function<String, double[]> {
    private final Map<String, double[]> map;

    public VariableValuesFunction(String string) throws InvalidVariableValuesFunction {
        String[] variablesValues = string.split(",");
        map = new HashMap<>(variablesValues.length);
        parse(variablesValues);
    }

    private void parse(String[] variablesValues) throws InvalidVariableValuesFunction {
        for (String vv : variablesValues) {
            String[] splitted = vv.split(":");
            if (splitted.length != 4) {
                throw new InvalidVariableValuesFunction(vv,
                        "4 colon-separated parameters expected, got " + splitted.length);
            }
            String name = splitted[0];
            double[] parsed = new double[3];
            for (int i = 1; i < splitted.length; i++) {
                try {
                    parsed[i - 1] = Double.parseDouble(splitted[i]);
                } catch (NumberFormatException e) {
                    throw new InvalidVariableValuesFunction(vv, "Invalid number \"" + splitted[i] + "\"", e);
                }
            }
            if (parsed[1] <= 0) {
                throw new InvalidVariableValuesFunction(vv, "Step cannot be <=0, got " + parsed[1]);
            }
            if (parsed[0] > parsed[2]) {
                throw new InvalidVariableValuesFunction(vv,
                        "Lower range value (" + parsed[0] + ") cannot be greater than the upper (" + parsed[2] + ")");
            }
            int n = (int) Math.floor((parsed[2] - parsed[0]) / parsed[1]) + 1;
            double[] values = new double[n];
            for (int i = 0; i < n; i++) {
                values[i] = parsed[0] + i * parsed[1];
            }
            map.put(name, values);
        }
    }

    public double[] getValuesOf(String variableName) {
        if (map.containsKey(variableName)) {
            return map.get(variableName);
        }
        return new double[0];
    }

    public List<Map<String, Double>> getValueTuplesList() throws ValueTuplesGenerationException {
        boolean tuplesWithSameLength = map.values().stream()
                .mapToInt(values -> values.length)
                .distinct()
                .count() == 1;
        if (!tuplesWithSameLength) {
            throw new ValueTuplesGenerationException("Provided variables have different length value tuples");
        }

        int length = map.values().iterator().next().length;
        List<Map<String, Double>> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            Map<String, Double> m = new HashMap<>(map.keySet().size());
            for (String varName : map.keySet()) {
                m.put(varName, map.get(varName)[i]);
            }
            list.add(m);
        }
        return list;
    }

    public List<Map<String, Double>> getValueTuplesGrid() {
        int length = map.values().stream()
                .mapToInt(values -> values.length)
                .reduce(1, (a, b) -> a * b);
        List<Map<String, Double>> list = new ArrayList<>(length);
        for (Map.Entry<String, double[]> variableValues : map.entrySet()) {
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
}
