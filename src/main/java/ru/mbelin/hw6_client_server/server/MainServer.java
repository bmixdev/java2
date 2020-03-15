package ru.mbelin.hw6_client_server.server;

import java.io.IOException;

public class MainServer {

    public static void main(String[] args) {
        ConsoleServer server = null;
        try {
             server = ConsoleServer.getServer(8188);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
