package org.example.compserver.models;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentResponseStats {
    private static final List<Double> responseTimesMillis = new ArrayList<>();

    private ConcurrentResponseStats() {
    }

    public static synchronized void addResponseTime(double milliseconds) {
        responseTimesMillis.add(milliseconds);
    }

    public static synchronized int responsesCount() {
        return responseTimesMillis.size();
    }

    public static synchronized double getAverageResponseTimeMillis() {
        return responseTimesMillis.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    public static synchronized double getMaxResponseTimeMillis() {
        return responseTimesMillis.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0);
    }


}
