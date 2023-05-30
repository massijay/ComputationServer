package org.example.compserver.models.expressions;

import java.util.Map;

public class Constant implements ExpressionNode {
    private final double value;

    public Constant(double value) {
        this.value = value;
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
    public double computeUsing(Map<String, Double> variableValues) {
        return value;
    }
}
