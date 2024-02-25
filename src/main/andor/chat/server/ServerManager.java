package main.andor.chat.server;

import main.andor.chat.client.User;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    private static final boolean CONNECT = true, DISCONNECT = false;
    private static final List<User> historicOfConnectedUsers = new ArrayList<>();

    private static final List<Socket> sockets = new ArrayList<>();

    public synchronized static void addUserToHistoric(User user) {
        historicOfConnectedUsers.add(user);
    }

    public static List<User> getHistoricOfUsers() {
        return historicOfConnectedUsers;
    }

    public synchronized static boolean isUserInHistoric(String nick) {
        for (User user:historicOfConnectedUsers) {
            if (user.getUsername().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public static User getUserFromHistoric(String nick) {
        for (User user: historicOfConnectedUsers) {
            if (user.getUsername().equals(nick)) {
                return user;
            }
        }
        return null;
    }

    public static void connect(User user) {
        manageConnection(user, CONNECT);
    }

    public static boolean isConnected(String nick) {
        for (User user:historicOfConnectedUsers) {
            if (user.getUsername().equals(nick)) {
                return user.isConnected();
            }
        }
        return false;
    }

    public static void disconnect(User user) {
        manageConnection(user, DISCONNECT);
    }

    public static int getAmountOfConnectedUsers() {
        int connectedUsers = 0;
        for (User u : historicOfConnectedUsers) {
            if (u.isConnected()) {
                connectedUsers++;
            }
        }
        return connectedUsers;
    }

    private synchronized static void manageConnection(User user, boolean state) {
        if (user != null) {
            User u = getUserFromHistoric(user.getUsername());
            if (u != null) {
                u.setConnected(state);
            }
        }
    }

    public static void addSocket(Socket socket) {
        sockets.add(socket);
    }

    public static void removeSocket(Socket socket) {
        sockets.remove(socket);
    }

    public static List<Socket> getSockets() {
        return sockets;
    }

}
