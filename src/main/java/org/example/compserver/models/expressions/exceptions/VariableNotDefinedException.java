package org.example.compserver.models.expressions.exceptions;

public class VariableNotDefinedException extends Exception{
    private final String variableName;

    public VariableNotDefinedException(String variableName) {
        super("Variable \"" + variableName + "\" not defined");
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}
