package org.example.compserver.models;

public class StatRequest implements Request {
    private final Type type;

    private StatRequest(Type type) {
        this.type = type;
    }

    public static StatRequest fromString(String requestString) {
        try {
            return new StatRequest(Type.valueOf(requestString));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        STAT_REQS, STAT_AVG_TIME, STAT_MAX_TIME
    }
}
