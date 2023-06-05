package org.example.compserver;

import org.example.compserver.models.ComputationServer;
import org.example.compserver.models.utils.Logger;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Logger logger = new Logger(System.out, System.err);
        if (args.length == 0) {
            logger.logError("Port number not provided, specify it as first parameter");
            return;
        }
        int port = Integer.parseInt(args[0]);

        ComputationServer server = new ComputationServer(port, logger);
        try {
            server.start();
        } catch (IOException e) {
            logger.logError("Error starting server, maybe provided port is already in use?\n\t" + e.getMessage());
        }

    }
}