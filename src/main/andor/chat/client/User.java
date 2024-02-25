package main.andor.chat.client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class User {

    private ArrayList<String> messageList;
    private final String username;
    private ClientHandler socket;
    private boolean connected;

    public User(String username, ClientHandler socket) {
        this.messageList = new ArrayList<>();
        this.username = username;
        this.socket = socket;
        this.connected = false;
    }

    public void addMessage(String text) {
        this.messageList.add(text);
    }

    public ArrayList<String> getConversationWhileDisconnected() {
        return this.messageList;
    }

    public void clearMessageList() {
        this.messageList.clear();
    }

    public String getUsername() {
        return username;
    }

    public String getChatUsername() {
        return "[" + this.username + "]";
    }

    public ClientHandler getSocket() {
        return socket;
    }

    public void setSocket(ClientHandler socket) {
        this.socket = socket;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
