package org.example.compserver.models.expressions;

import org.example.compserver.models.expressions.exceptions.InvalidExpressionException;
import org.example.compserver.models.expressions.exceptions.OperatorNotSupportedException;
import org.example.compserver.models.expressions.exceptions.VariableNotDefinedException;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    private int cursor = 0;
    private final String string;
    private final ExpressionNode root;
    private final Set<String> variables;

    public Expression(String string) throws InvalidExpressionException {
        this.string = string;
        variables = new HashSet<>();
        root = parse();
    }

    private ExpressionNode parse() throws InvalidExpressionException {
        Token token = TokenType.CONSTANT.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;
            return new Constant(Double.parseDouble(string.substring(token.start, token.end)));
        }
        token = TokenType.VARIABLE.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;
            String var = string.substring(token.start, token.end);
            variables.add(var);
            return new Variable(var);
        }
        token = TokenType.OPEN_PARENTHESIS.next(string, cursor);
        if (token == null || token.start != cursor) {
            throw new InvalidExpressionException(string, cursor, "\"(\"");
        }
        cursor = token.end;
        ExpressionNode left = parse();
        token = TokenType.OPERATOR.next(string, cursor);
        if (token == null || token.start != cursor) {
            throw new InvalidExpressionException(string, cursor, "valid operator symbol");
        }
        int opPos = token.start;
        cursor = token.end;
        ExpressionNode right = parse();
        token = TokenType.CLOSED_PARENTHESIS.next(string, cursor);
        if (token == null || token.start != cursor) {
            throw new InvalidExpressionException(string, cursor, "\")\"");
        }
        cursor = token.end;
        try {
            return new Operator(string.charAt(opPos), left, right);
        } catch (OperatorNotSupportedException e) {
            throw new InvalidExpressionException(string, opPos, "valid operator symbol", e);
        }
    }

    public double computeUsing(Map<String, Double> variableValues) throws VariableNotDefinedException {
        return root.computeUsing(variableValues);
    }

    public Set<String> getVariables() {
        return variables;
    }

    private enum TokenType {
        CONSTANT(Pattern.compile("[0-9]+(\\.[0-9]+)?")),
        VARIABLE(Pattern.compile("[a-z][a-z0-9]*")),
        OPERATOR(Pattern.compile("[+\\-*/^]")),
        OPEN_PARENTHESIS(Pattern.compile("\\(")),
        CLOSED_PARENTHESIS(Pattern.compile("\\)"));
        private final Pattern pattern;

        TokenType(Pattern pattern) {//TODO
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
