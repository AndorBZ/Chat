package main.andor.chat.client;

import main.andor.chat.server.ServerManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Scanner inFromClient;
    private final PrintWriter outFromServer;
    private User user;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.inFromClient = new Scanner(socket.getInputStream());
        this.outFromServer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            disconnectClient(this.socket);
        }));

        connectUser();
        getMessagesWhileDisconect();
        startChatting();
    }

    /**
     * Verifica si ya hay un usuario conectado con el mismo nick introducido.
     * También verifica si dicho usuario se ha conectado anteriormente.
     * Si no es así, se le añade al historial de usuarios conectados.
     */
    private void connectUser() {
        outFromServer.println("""
                
                Introduce tu nick:""");

        while (true) {
            String input = inFromClient.nextLine();
            if (!input.isEmpty()) {
                if (!ServerManager.isConnected(input)) {
                    if(!ServerManager.isUserInHistoric(input)) {
                        this.user = new User(input, this);
                        ServerManager.addUserToHistoric(this.user);
                        outFromServer.print("Bienvenido/a al chat, " + this.user.getChatUsername() + ".");
                    } else {
                        this.user = ServerManager.getUserFromHistoric(input);
                        if (this.user != null) {
                            this.user.setSocket(this);
                            outFromServer.print("Hola de nuevo, " + this.user.getChatUsername() + ".");
                        }
                    }
                    ServerManager.connect(user);
                    broadCastConection(true);
                    outFromServer.println(" (" + ServerManager.getAmountOfConnectedUsers() + ") Conectado/s");
                    break;
                } else {
                    outFromServer.println("El usuario [" + input + "] ya está en el chat.\nIntroduce tu nick:");
                }
            }
        }
    }

    /**
     * Muestra los mensajes enviados ante la ausencia del usuario.
     */
    private void getMessagesWhileDisconect() {
        for (User user : ServerManager.getHistoricOfUsers()) {
            if (user.equals(this.user)) {
                for (String message : user.getConversationWhileDisconnected()) {
                    outFromServer.println(message);
                }
            }
        }
        this.user.clearMessageList();
    }

    /**
     * Si el mensaje es diferente de 'bye', envía el mensaje a todos los usuarios conectados.
     */
    private void startChatting() {
        while (true) {
            if (inFromClient.hasNextLine()) {
                String clientSentence = inFromClient.nextLine();
                if (clientSentence.toLowerCase().trim().equals("bye")) {
                    outFromServer.println("Adios, " + this.user.getUsername());
                    ServerManager.disconnect(this.user);
                    broadCastConection(false);
                    disconnectClient(this.socket);
                    break;
                }
                broadCast(clientSentence);
            }
        }
    }

    /**
     * Accede al historial de usuarios conectados alguna vez al servidor.
     * Si el usuario está conectado: envia el mensaje diréctamente.
     * Si el usuario está desconectado: envía el mensaje a su lista de mensajes perdidos por ausencia.
     * @param text Mensaje escrito por el usuario
     */
    private void broadCast(String text) {
        for (User otherUser:ServerManager.getHistoricOfUsers()) {
            if (!otherUser.getUsername().equals(this.user.getUsername())) {
                if (otherUser.isConnected()) {
                    otherUser.getSocket().sendMessage(this.user.getChatUsername() + ": " + text);
                } else {
                otherUser.addMessage(this.user.getChatUsername() + ": " + text);
                }
            }
        }
    }

    /**
     * Informa a los usuarios sobre el estado de conexión del usuario actual.
     * @param connection dependiendo de este valor booleano, se enviará un mensaje a los demás usuarios informando
     *                   de la conexión o desconexión del usuario en cuestión.
     */
    private void broadCastConection(boolean connection) {
        for (User otherUser:ServerManager.getHistoricOfUsers()) {
            if (!otherUser.getUsername().equals(this.user.getUsername())) {
                if (connection) {
                    otherUser.getSocket().sendMessage(this.user.getChatUsername() + " se ha conectado");
                } else {
                    otherUser.getSocket().sendMessage(this.user.getChatUsername() + " se ha desconectado");
                }
            }
        }
    }

    /**
     * Envía el mensaje a todos los usuarios.
     * @param text mensaje escrito por el usuario actual.
     */
    private void sendMessage(String text) {
        outFromServer.println(text);
    }

    private static void disconnectClient(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}