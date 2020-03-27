package ru.mbelin.server;

public interface IClientSocketAction {
    boolean authentication(ClientSocketThread socket, String command);
    void subscribe(ClientSocketThread socket);
    void unsubscribe(ClientSocketThread socket);
    void sendMessageAllClients(ClientSocketThread socket, String message);
    void sendMessageToClient(ClientSocketThread socket, String user, String message);
    void executeCommand(ClientSocketThread socket, String command);
    void sendForward(ClientSocketThread socket, String message);
}
