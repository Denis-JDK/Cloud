package io;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IoFileCommandServer {

    public IoFileCommandServer() {

        try (ServerSocket server = new ServerSocket(8189)) { // траверс ресурс вызывается автоматически server.close если брейк какой то.
            System.out.println("Server started on port 8189");
            while (true) {
                try {
                    Socket socket = server.accept(); //получили сокет некое соединение
                    System.out.println("user accepted");
                    Handler handler = new Handler(this, socket);
                    new Thread(handler).start();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new IoFileCommandServer();
    }
}
