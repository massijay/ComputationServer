package org.example.compserver.models.expressions.exceptions;

public class OperatorNotSupportedException extends Exception {
    private final char symbol;

    public OperatorNotSupportedException(char symbol) {
        super("Operator '" + symbol + "' not supported");
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
