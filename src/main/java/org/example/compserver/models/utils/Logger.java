package org.example.compserver.models.utils;

import java.io.OutputStream;
import java.io.PrintStream;

public class Logger {
    private final PrintStream ps;
    private final PrintStream errorPs;

    public Logger(OutputStream os, OutputStream errorOs) {
        this.ps = new PrintStream(os);
        this.errorPs = new PrintStream(errorOs);
    }

    public void logInfo(String message) {
        ps.println(formatLine(message));
    }

    public void logError(String message) {
        errorPs.println(formatLine(message));
    }

    private String formatLine(String line) {
        return String.format("[%1$tY-%1$tm-%1$td %1$tT] %2$s",
                System.currentTimeMillis(),
                line);
    }
}
