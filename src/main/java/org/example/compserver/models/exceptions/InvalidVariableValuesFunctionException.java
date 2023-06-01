package org.example.compserver.models.exceptions;

public class InvalidVariableValuesFunctionException extends Exception {
    private final String variableValues;

    public InvalidVariableValuesFunctionException(String variableValues, String message, Throwable cause) {
        super(message, cause);
        this.variableValues = variableValues;
    }

    public InvalidVariableValuesFunctionException(String variableValues, String message) {
        super(message);
        this.variableValues = variableValues;
    }

    public String getVariableValues() {
        return variableValues;
    }
}
