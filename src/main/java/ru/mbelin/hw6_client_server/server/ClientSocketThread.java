package ru.mbelin.hw6_client_server.server;

import ru.mbelin.hw6_client_server.ConstantMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.mbelin.hw6_client_server.server.ConsoleServer.COMMAND_END;

public class ClientSocketThread extends Thread {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String uuid;
    private String user;
    private ClientSocketThread instance;

    public ClientSocketThread(Socket socket) {
        this.socket = socket;
        try {
            inputStream = new DataInputStream(this.socket.getInputStream());
            outputStream = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    private boolean getSystemInfo(String msg) {
        Pattern pattern = Pattern.compile("#\\[(.+?):(.+?)\\]#");
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            String paramCode = matcher.group(1);
            String paramValue = matcher.group(2);
            //ConsoleColors.print(matcher.group(1).toString(), ConsoleColors.GREEN_BOLD_BRIGHT);
            //ConsoleColors.print(matcher.group(2).toString(),ConsoleColors.GREEN);
            switch (paramCode.toUpperCase()) {
                case ConstantMessage.SYSCODE_UUID :
                    uuid = paramValue;
                    break;
                case ConstantMessage.SYSCODE_USER :
                    user = paramValue;
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (true) {
            String msgFromClient = null;
            try {
                msgFromClient = inputStream.readUTF();
                if (getSystemInfo(msgFromClient)) continue;
                if (msgFromClient.toUpperCase().equals(COMMAND_END.toUpperCase())) {
                    System.out.printf("Клиент %s отключился\n", user);
                    interrupt();
                    break;
                }
                System.out.printf("[%s]->: %s\n", user, msgFromClient);
                //outputStream.writeUTF("Эхо: " + msgFromClient);
            }
            catch (SocketException e) {
                    try {
                        this.finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    } finally {
                        break;
                    }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String msg) throws IOException {
            outputStream.writeUTF(msg);
    }

    public void close() {
        try {
            inputStream.close();;
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientSocketThread that = (ClientSocketThread) o;
        return Objects.equals(socket, that.socket) &&
                Objects.equals(inputStream, that.inputStream) &&
                Objects.equals(outputStream, that.outputStream) &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, inputStream, outputStream, uuid, user);
    }

    @Override
    public String toString() {
        return "ClientSocketThread{" +
                "socket=" + socket +
                ", uuid='" + uuid + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
