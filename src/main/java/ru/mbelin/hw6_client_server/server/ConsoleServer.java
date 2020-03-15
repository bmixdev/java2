package ru.mbelin.hw6_client_server.server;

import ru.mbelin.hw6_client_server.ConstantMessage;
import ru.mbelin.utils.Color;
import ru.mbelin.utils.ConsoleColors;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleServer {

    private static ConsoleServer instance;
    private int port;
    private ServerSocket serverSocket;
    private LinkedList<ClientSocketThread> sockets;
    private Thread socketListener;
    private Thread inputThread;

    public final static String COMMAND_END = "#END#";

    private ConsoleServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.sockets = new LinkedList<>();
        this.socketListener = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket clientSocket;
                while (true) {
                    try {
                        if (!socketListener.isInterrupted()) {
                            if (serverSocket == null) throw new NullPointerException("Сервер не инициализирован!");
                            if (!serverSocket.isClosed()) {
                                ConsoleColors.print("Ожидаю новых подключений:", ConsoleColors.PURPLE_UNDERLINED);
                                clientSocket = serverSocket.accept();
                                sockets.add(new ClientSocketThread(clientSocket));
                                ConsoleColors.print("Подключился новый клиент: " + clientSocket.toString(), Color.BLUE);
                            }
                        } else
                            throw new InterruptedException();
                    }
                    catch (SocketException ignore) {}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        ConsoleColors.print("<< Сервер запущен <<"+ InetAddress.getLocalHost().getHostAddress() + ":" + port+">> >>", ConsoleColors.CYAN_UNDERLINED);
        this.socketListener.start();
        this.inputThread = new Thread(this::doInput);
        this.inputThread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printServerCommand();
    }

    public static ConsoleServer getServer(int port) throws IOException {
        if (instance ==  null) {
            instance = new ConsoleServer(port);
        } else
        {
            if (instance.port != port)
                throw new RuntimeException("ConsoleServer уже запущен на порте" + instance.port);
        }
        return instance;
    }

    private void printServerCommand() {
        ConsoleColors.print("******************************************************************************************", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("Список допустимых команд сервера:", ConsoleColors.GREEN_UNDERLINED);
        ConsoleColors.print("******************************************************************************************", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("[СООБЩЕНИЕ] - будет отправлено произвольное сообщение всем клиентам", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("#users - вывести список клиентов", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("#kill [ПОЛЬЗОВАТЕЛЬ(USER)] - завершить сессию клиента", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("#to [ПОЛЬЗОВАТЕЛЬ(USER)] [СООБЩЕНИЕ] - отправить сообщение конкретному пользователю", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("#end - завершить работу сервера", ConsoleColors.YELLOW_BOLD);
        ConsoleColors.print("******************************************************************************************", ConsoleColors.YELLOW_BOLD);
    }



    private boolean executeSystemCommand(String msg, Boolean needBreak) {
        Pattern pattern = Pattern.compile("^#(.+?)( |$)");
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            String paramCode = matcher.group(1);
            //ConsoleColors.print(matcher.group(1).toString(), ConsoleColors.GREEN_BOLD_BRIGHT);
            //ConsoleColors.print(matcher.group(2).toString(),ConsoleColors.GREEN);
            switch (paramCode.toUpperCase()) {
                case ConstantMessage.CMD_END : {
                        ConsoleColors.print("Выход из консольного ввода", ConsoleColors.PURPLE_UNDERLINED);
                        if (this.socketListener.isAlive())
                            this.socketListener.interrupt();
                        while (!this.socketListener.isInterrupted()) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        close();
                        needBreak = true;
                    break;
                }
                case ConstantMessage.CMD_USERLIST : {
                    ConsoleColors.print("Список подключенных пользователей", ConsoleColors.PURPLE_UNDERLINED);
                    sockets.forEach((t) -> System.out.println(t));
                    break;
                }
                case  ConstantMessage.CMD_KILL : {
                    pattern = Pattern.compile("^#(.+?) (.+?)$");
                    matcher = pattern.matcher(msg);
                    if (matcher.find()) {
                        killUser(matcher.group(2));
                    }
                    break;
                }
                case  ConstantMessage.CMD_TO_USER : {
                    pattern = Pattern.compile("^#(.+?) (.+?) (.+?)$");
                    matcher = pattern.matcher(msg);
                    if (matcher.find()) {
                        sendToUser(matcher.group(2), matcher.group(3));
                    }
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private void killUser(String user) {
        ClientSocketThread socketThread = null;
        for (ClientSocketThread s: sockets) {
            if (s.getUser().toUpperCase().equals(user.toUpperCase())) {
                socketThread = s;
                sendToSocket(s, ConstantMessage.CMD_END);
                break;
            }
        }
        if (socketThread != null)
            sockets.remove(socketThread);
    }


    private void sendToUser(String user, String msg) {
        for (ClientSocketThread s: sockets) {
            if (s.getUser().toUpperCase().equals(user.toUpperCase())) {
                sendToSocket(s, msg);
                break;
            }
        }
    }

    private void doInput() {
        ConsoleColors.print("Консоль ответа для клиентов готова:" , ConsoleColors.PURPLE_UNDERLINED);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            checkSockets();
            Boolean needBreak = false;
            if (executeSystemCommand(str, needBreak)) { if (needBreak) break; else continue; };
            sendToAll(str);
        }
    }

    private void checkSockets() {
        LinkedList<ClientSocketThread> tmp = new LinkedList<>();
        for (ClientSocketThread s: sockets) {
            if (s != null) tmp.add(s);
        }
        sockets = tmp;
    }

    private void sendToAll(String str) {
        checkSockets();
        for (ClientSocketThread s: sockets) {
            sendToSocket(s, str);
        }
    }

    private void sendToSocket(ClientSocketThread socket, String msg) {
        try {
            if (!msg.trim().isEmpty())
                socket.sendMessage(msg);
        } catch (SocketException e)
        {
            if ( e.toString().indexOf("reset") > 0 )
                sockets.remove(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<ClientSocketThread> getSockets() {
        return sockets;
    }

    public Thread getSocketListener() {
        return socketListener;
    }

    public Thread getInputThread() {
        return inputThread;
    }

    public void close() {
        for (ClientSocketThread socket: sockets) {
            socket.close();
        }
        try {
            serverSocket.close();
            serverSocket = null;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            ConsoleColors.print("<< Сервер остановлен >>" , ConsoleColors.CYAN_UNDERLINED);
            System.exit(1);
        }
    }

}
