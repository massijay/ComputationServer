package org.example.compserver.models.expressions;

import org.example.compserver.models.expressions.exceptions.OperatorNotSupportedException;
import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Operator implements ExpressionNode {
    private final Type type;
    private final ExpressionNode leftChild;
    private final ExpressionNode rightChild;

    public Operator(char symbol, ExpressionNode leftOperand, ExpressionNode rightOperand)
            throws OperatorNotSupportedException {
        this.leftChild = leftOperand;
        this.rightChild = rightOperand;
        this.type = Type.fromSymbol(symbol);
    }

    @Override
    public ExpressionNode getLeftChild() {
        return leftChild;
    }

    @Override
    public ExpressionNode getRightChild() {
        return rightChild;
    }

    @Override
    public double computeUsing(Map<String, Double> variableValues) throws VariableNotDefinedException {
        return type.getOperation().apply(leftChild.computeUsing(variableValues), rightChild.computeUsing(variableValues));
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        if (type != operator.type) return false;
        if (!Objects.equals(leftChild, operator.leftChild)) return false;
        return Objects.equals(rightChild, operator.rightChild);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, leftChild, rightChild);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("(")
                .append(leftChild.toString())
                .append(type.getSymbol())
                .append(rightChild.toString())
                .append(")")
                .toString();
    }

    public enum Type {
        ADDITION('+', Double::sum),
        SUBTRACTION('-', (m, s) -> m - s),
        MULTIPLICATION('*', (a, b) -> a * b),
        DIVISION('/', (n, d) -> n / d),
        POWER('^', Math::pow);

        private static final Map<Character, Type> map =
                Arrays.stream(Type.values())
                        .collect(Collectors.toMap(Type::getSymbol, t -> t));
        private final char symbol;
        private final BinaryOperator<Double> operation;

        Type(char symbol, BinaryOperator<Double> operation) {
            this.symbol = symbol;
            this.operation = operation;
        }

        public char getSymbol() {
            return symbol;
        }

        public BinaryOperator<Double> getOperation() {
            return operation;
        }

        public static Type fromSymbol(char symbol) throws OperatorNotSupportedException {
            if (map.containsKey(symbol)) {
                return map.get(symbol);
            }
            throw new OperatorNotSupportedException(symbol);
        }
    }
}
