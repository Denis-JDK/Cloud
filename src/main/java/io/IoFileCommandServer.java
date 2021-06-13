package io;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IoFileCommandServer {
    private Map<String, Handler> clients;
    private int counter;

    public IoFileCommandServer() {
            counter = 0;
        try (ServerSocket server = new ServerSocket(8189)) { // траверс ресурс вызывается автоматически server.close если брейк какой то.
            SQLHandler.connect();
            clients = new ConcurrentHashMap<>();
            System.out.println("Server started on port 8189");
            while (true) {
                try {
                    Socket socket = server.accept(); //получили сокет некое соединение
                    System.out.println("user accepted");
                    Handler handler = new Handler(this, socket, "Клиент #" + ++counter);
                    subscribe(handler);
                    new Thread(handler).start();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            SQLHandler.disconnect();
        }
    }

    public void subscribe (Handler client) {
        clients.put(client.getNick(), client);
    }
    public void unsubscribe (Handler client) {
        clients.remove(client.getNick());
    }

    public void isNickInChat(String client) {
        return; //clients.containsKey(nickname);
    }

    public void broadcastMsg (String msg) {
        for (Handler c: clients.values()) {
        }
    }

    public void unicalMsg (String nickname, String msg) {
        //if (isNickInChat(nickname))
        {
            Handler handler = clients.get(nickname);
            //handler.setMsg();
        }
    }

    public static void main(String[] args) {
        new IoFileCommandServer();
    }
}
