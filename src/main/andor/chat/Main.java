package main.andor.chat;

import main.andor.chat.server.ChatServer;

public class Main {

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }

}
