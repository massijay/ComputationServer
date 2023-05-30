package org.example.compserver.models.expressions;

import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.Map;

public interface ExpressionNode {
    ExpressionNode getLeftChild();

    ExpressionNode getRightChild();

    double computeUsing(Map<String, Double> variableValues) throws VariableNotDefinedException;
}
