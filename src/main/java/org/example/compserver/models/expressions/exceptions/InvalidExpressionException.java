package org.example.compserver.models.expressions.exceptions;

public class InvalidExpressionException extends Exception {
    private final String expressionString;
    private final int errorPosition;
    private final String expectedToken;

    public InvalidExpressionException(String expressionString, int errorPosition, String expectedToken) {
        super(buildErrorMessage(expressionString, errorPosition, expectedToken));
        this.expressionString = expressionString;
        this.errorPosition = errorPosition;
        this.expectedToken = expectedToken;
    }

    public InvalidExpressionException(String expressionString, int errorPosition, String expectedToken, Throwable cause) {
        super(buildErrorMessage(expressionString, errorPosition, expectedToken), cause);
        this.expressionString = expressionString;
        this.errorPosition = errorPosition;
        this.expectedToken = expectedToken;
    }

    private static String buildErrorMessage(String expressionString, int errorPosition, String expectedToken) {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected token encountered parsing the expression string, got\n")
                .append(expressionString)
                .append(" ".repeat(Math.max(0, errorPosition)))
                .append("^\n")
                .append("Expected: ")
                .append(expectedToken);
        return sb.toString();
    }

    public String getExpressionString() {
        return expressionString;
    }

    public int getErrorPosition() {
        return errorPosition;
    }

    public String getExpectedToken() {
        return expectedToken;
    }
}
