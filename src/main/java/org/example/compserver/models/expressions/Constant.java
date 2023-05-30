package org.example.compserver.models.expressions;

import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constant constant = (Constant) o;
        return Double.compare(constant.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
