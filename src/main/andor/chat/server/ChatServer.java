package main.andor.chat.server;

import main.andor.chat.client.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static volatile boolean isShutdownRequested = false;
    private final int port = 6789;

    public void start() {

        /**
         * Cuando detecta que el servidor se va a apagar, envía un mensaje a todos los clientes.
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Socket socket : ServerManager.getSockets()) {
                adviceClients(socket);
            }
            Thread.currentThread().interrupt();
        }));

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado y escuchando en el puerto " + port);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread thread = new Thread(clientHandler);
                    executor.execute(thread);
                    ServerManager.addSocket(socket);
                } catch (IOException e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }

    }

    private void adviceClients(Socket socket) {
        try (PrintWriter output = new PrintWriter(socket.getOutputStream(), true)){
            output.println("El servidor va a cerrar.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
