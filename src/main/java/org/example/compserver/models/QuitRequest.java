package org.example.compserver.models;

public class QuitRequest implements Request {
    private static final String QUIT_COMMAND = "BYE";
    private static final QuitRequest SINGLE_INSTANCE = new QuitRequest();

    private QuitRequest() {
    }

    public static QuitRequest fromString(String requestString) {
        if (requestString.equals(QUIT_COMMAND)) {
            return SINGLE_INSTANCE;
        }
        return null;
    }
}
