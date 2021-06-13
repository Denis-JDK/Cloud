package io;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network implements Closeable {

    private final DataInputStream is;
    private final DataOutputStream os;
    private final Socket socket;
    private static Network instance;
    private IoFileCommandServer server;



    public static Network get() throws IOException {
        if (instance == null) {  //если у нас не было инстанса то делаем новый
            instance = new Network(); // если есть то его же и возвращаем
        }
        return instance;
    }

    private Network() throws IOException {
        socket = new Socket ( "localhost" ,8189);
        os = new DataOutputStream(socket.getOutputStream()); //проинициализировали стримы
        is = new DataInputStream(socket.getInputStream());
    }

    public void write (String message) throws IOException {
        os.writeUTF(message);
        os.flush();
    }
    public int read(byte[] buffer) throws IOException {
        return is.read(buffer);
    }

    public long readLong() throws IOException {
        return is.readLong();
    }

    public String read () throws IOException {
        return is.readUTF();
    }

    public void close () throws IOException {
        is.close();
        os.close();
        socket.close();
    }
}
