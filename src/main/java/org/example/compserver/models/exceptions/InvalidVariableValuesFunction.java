package org.example.compserver.models.exceptions;

public class InvalidVariableValuesFunction extends Exception {
    private final String variableValues;

    public InvalidVariableValuesFunction(String variableValues, String message, Throwable cause) {
        super(message, cause);
        this.variableValues = variableValues;
    }

    public InvalidVariableValuesFunction(String variableValues, String message) {
        super(message);
        this.variableValues = variableValues;
    }

    public String getVariableValues() {
        return variableValues;
    }
}
