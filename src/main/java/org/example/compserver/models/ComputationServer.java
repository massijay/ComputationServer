package org.example.compserver.models;

import org.example.compserver.models.exceptions.MalformedRequestException;
import org.example.compserver.models.utils.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputationServer {
    private final int port;
    private final Logger logger;
    private final ExecutorService executorService;
    private final static int CONCURRENT_CLIENTS = 100;
    private static final String QUIT_COMMAND = "BYE";

    public ComputationServer(int port, Logger logger) {
        this.port = port;
        this.logger = logger;
        this.executorService = Executors.newFixedThreadPool(CONCURRENT_CLIENTS);
    }

    public void start() throws IOException {
        logger.logInfo("Begin serving on port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    final Socket socket = serverSocket.accept();

                    executorService.submit(() -> {
                        try (socket) {
                            logger.logInfo("Client connected");
                            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                            while (true) {
                                String command = br.readLine();
                                if (command == null) {
                                    logger.logInfo("Client abruptly closed connection");
                                    break;
                                }
                                if (command.equals(QUIT_COMMAND)) {
                                    logger.logInfo("Client closed connection");
                                    break;
                                }
                                bw.write(process(command) + System.lineSeparator());
                                bw.flush();
                            }
                        } catch (IOException e) {
                            logger.logError("Connection error\n\t" + e.getMessage());
                        }
                    });

                } catch (IOException e) {
                    logger.logError("Error during client connection\n\t" + e.getMessage());
                }
            }
        } finally {
            executorService.shutdown();
        }
    }

    private String process(String input) {
        long start = System.nanoTime();
        Request request = null;
        if ((request = StatRequest.fromString(input)) != null) {
            double stat = switch (((StatRequest) request).getType()) {
                case STAT_REQS -> ConcurrentResponseStats.responsesCount();
                case STAT_AVG_TIME -> ConcurrentResponseStats.getAverageResponseTimeMillis() / 1e3;
                case STAT_MAX_TIME -> ConcurrentResponseStats.getMaxResponseTimeMillis() / 1e3;
            };
            double millis = (System.nanoTime() - start) / 1e6;
            ConcurrentResponseStats.addResponseTime(millis);
            return Response.buildOk(millis, stat);
        }
        try {
            if ((request = ComputationRequest.fromString(input)) != null) {
                double result = ComputationService.submit((ComputationRequest) request).get();
                double millis = (System.nanoTime() - start) / 1e6;
                ConcurrentResponseStats.addResponseTime(millis);
                return Response.buildOk(millis, result);
            }
        } catch (MalformedRequestException | ExecutionException e) {
            if (e.getCause() != null) {
                return Response.buildError(e.getCause().getClass().getSimpleName(), e.getCause().getMessage());
            }
            return Response.buildError(e.getClass().getSimpleName(), e.getMessage());
        } catch (InterruptedException e) {
            logger.logError("Thread interrupted\n\t" + e.getMessage());
        }
        return Response.buildError("Malformed request");
    }
}
