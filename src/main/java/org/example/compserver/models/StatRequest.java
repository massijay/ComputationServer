package org.example.compserver.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatRequest that = (StatRequest) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public enum Type {
        STAT_REQS, STAT_AVG_TIME, STAT_MAX_TIME
    }
}
