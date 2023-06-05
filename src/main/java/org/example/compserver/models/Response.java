package org.example.compserver.models;

import java.util.Locale;

public interface Response {
    static String buildOk(double responseTimeMillis, double result) {
        return String.format(Locale.ROOT, "OK;%.3f;%.6f", responseTimeMillis / 1e3, result);
    }

    static String buildError(String errorMessage) {
        return "ERR;" + errorMessage;
    }
}
