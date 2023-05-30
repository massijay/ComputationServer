package org.example.compserver.models.expressions;

import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.Map;

public class Variable implements ExpressionNode {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public ExpressionNode getLeftChild() {
        return null;
    }

    @Override
    public ExpressionNode getRightChild() {
        return null;
    }

    @Override
    public double computeUsing(Map<String, Double> variableValues) throws VariableNotDefinedException {
        if(variableValues.containsKey(name)){
            return variableValues.get(name);
        }
        throw new VariableNotDefinedException(name);
    }
}
