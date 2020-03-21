package ru.mbelin.server;

import ru.mbelin.server.service.UserData;
import ru.mbelin.utils.ConstantMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.mbelin.server.service.BaseAuthService;

public class ClientSocketThread extends Thread {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private UserData userData;
    private ClientSocketThread instance;
    private IClientSocketAction socketAction;
    private boolean isAuthorized;

    public ClientSocketThread(Socket socket, IClientSocketAction socketAction) throws IOException {
        this.socket = socket;
        this.socketAction = socketAction;
        this.inputStream = new DataInputStream(this.socket.getInputStream());
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        this.isAuthorized = false;
        start();
        socketAction.executeCommand(this, ConstantMessage.CMD_PRINT_FIRST);
    }

    public void setUserDate(UserData userData) {
        this.userData = userData;
    }


    private boolean getSystemInfo(String msg) {
        if (msg.startsWith("#")) {
            try {
            socketAction.executeCommand(this, msg);
            } catch (RuntimeException e) {
                socketAction.sendForward(this, ConsoleServer.errorMsg(e.getMessage()));
            }
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        String msgFromClient;
        while (true) {
            try {
                msgFromClient = inputStream.readUTF();
                if (!isAuthorized) {
                    if (!socketAction.authentication(this, msgFromClient)) {
                        continue;
                    } else {
                        socketAction.subscribe(this);
                        this.isAuthorized = true;
                        continue;
                    }
                }

                if (userData != null && !userData.getLogin().isEmpty()) {
                    System.out.printf("\t[%s]: %s%n", userData.getLogin(), msgFromClient);
                }

                // обработка системных сообщений
                if (getSystemInfo(msgFromClient)) continue;

                socketAction.sendMessageAllClients(this, msgFromClient);
            }
            catch (SocketException se) {
                if (se.getMessage().toUpperCase().indexOf("CLOSED") == 0 && se.getMessage().toUpperCase().indexOf("RESET") == 0)
                    se.printStackTrace();
                break;
            }
            catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        close();
    }

    public void sendMessage(String msg) throws IOException {
        outputStream.writeUTF(msg);
    }

    public void close() {
        try {
            socketAction.unsubscribe(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return userData.getLogin();
    }

    public String getUuid() {
        return userData.getUid();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientSocketThread that = (ClientSocketThread) o;
        return Objects.equals(socket, that.socket) &&
                Objects.equals(inputStream, that.inputStream) &&
                Objects.equals(outputStream, that.outputStream) &&
                Objects.equals(userData, that.userData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, inputStream, outputStream, userData);
    }

    @Override
    public String toString() {
        return "ClientSocketThread{" +
                "socket=" + socket +
                ", userData=" + userData +
                '}';
    }
}
