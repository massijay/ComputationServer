package org.example.compserver.models.exceptions;

public class MalformedRequestException extends Exception {
    private final String requestString;

    public MalformedRequestException(String requestString, String message, Throwable cause) {
        super(message, cause);
        this.requestString = requestString;
    }

    public MalformedRequestException(String requestString, String message) {
        super(message);
        this.requestString = requestString;
    }

    public String getRequestString() {
        return requestString;
    }
}
