package org.example.compserver.models.expressions;

import org.example.compserver.models.expressions.exceptions.OperatorNotSupportedException;
import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    private int cursor = 0;
    private final String string;
    private final ExpressionNode root;

    public Expression(String string) {
        this.string = string;
        root = parse();
    }

    private ExpressionNode parse() {
        Token token = TokenType.CONSTANT.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;
            return new Constant(Double.parseDouble(string.substring(token.start, token.end)));
        }
        token = TokenType.VARIABLE.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;
            return new Variable(string.substring(token.start, token.end));
        }
        token = TokenType.OPEN_PARENTHESIS.next(string, cursor);
        if (token == null || token.start != cursor) {
            throw new IllegalArgumentException("ILLEGALE"); // TODO
        }
        cursor = token.end;
        ExpressionNode left = parse();
        token = TokenType.OPERATOR.next(string, cursor);
        if (token == null || token.start != cursor) {
            throw new IllegalArgumentException("ILLEGALE"); // TODO
        }
        int opPos = token.start;
        cursor = token.end;
        ExpressionNode right = parse();
        token = TokenType.CLOSED_PARENTHESIS.next(string, cursor);
        if (token == null || token.start != cursor) {
            throw new IllegalArgumentException("ILLEGALE"); // TODO
        }
        cursor = token.end;
        try {
            return new Operator(string.charAt(opPos), left, right);
        } catch (OperatorNotSupportedException e) { // TODO SHOULD BE UNCHECKED??
            throw new RuntimeException(e);
        }
    }

    public double computeUsing(Map<String, Double> variableValues) throws VariableNotDefinedException {
        return root.computeUsing(variableValues);
    }

    private enum TokenType {
        CONSTANT(Pattern.compile("[0-9]+(\\.[0-9]+)?")),
        VARIABLE(Pattern.compile("[a-z][a-z0-9]*")),
        OPERATOR(Pattern.compile("[+\\-*/^]")),
        OPEN_PARENTHESIS(Pattern.compile("\\(")),
        CLOSED_PARENTHESIS(Pattern.compile("\\)"));
        private final Pattern pattern;

        TokenType(Pattern pattern) {
            this.pattern = pattern;
        }

        private Token next(String s, int i) {
            Matcher matcher = pattern.matcher(s);
            if (!matcher.find(i)) {
                return null;
            }
            return new Token(matcher.start(), matcher.end());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return Objects.equals(root, that.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }

    @Override
    public String toString() {
        return root.toString();
    }

    private record Token(int start, int end) {
    }
}
