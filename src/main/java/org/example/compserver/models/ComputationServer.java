package org.example.compserver.models;

import org.example.compserver.models.exceptions.MalformedRequestException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputationServer {
    private final int port;
    private final ExecutorService executorService;
    private final static int CONCURRENT_CLIENTS = 100;
    private static final String QUIT_COMMAND = "BYE";

    public ComputationServer(int port) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(CONCURRENT_CLIENTS);
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    final Socket socket = serverSocket.accept();

                    executorService.submit(() -> {
                        try (socket) {
                            // Use a printwriter?
                            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                            while (true) {
                                String command = br.readLine();
                                if (command == null) {
                                    // Handle Client abruptly closed connection
                                    break;
                                }
                                if (command.equals(QUIT_COMMAND)) {
                                    // Handle Client gracefully closed connection
                                    break;
                                }
                                bw.write(process(command) + System.lineSeparator());
                                bw.flush();
                            }


                        } catch (IOException e) {
                            // Catch IO errors
                        }
                    });

                } catch (IOException e) {
                    // Catch client connection problems
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
        } catch (MalformedRequestException e) {
            // Handle parsing exception
        } catch (ExecutionException e) {
            // Handle ValueTuplesGenerationException, VariableNotDefinedException
        } catch (InterruptedException e) {
            // Handle task interrupted
        }

        //RITORNA ERRORE RICHIESTA SCONOSCIUTA

        return "";
    }
}
