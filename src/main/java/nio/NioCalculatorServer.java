package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class NioCalculatorServer {
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer buffer;
    private Map<Selector, Integer> clients;


    public NioCalculatorServer() throws IOException {
        buffer = ByteBuffer.allocate(256);
        serverChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverChannel.bind(new InetSocketAddress(8289));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, OP_ACCEPT);
        while (serverChannel.isOpen()) {
            selector.select();  //block  проходит сбор всех событий со всех каналов
            Set<SelectionKey> keys = selector.selectedKeys(); //наполнили коллекцию событиями ключиками
            Iterator<SelectionKey> keyIterator = keys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        handlerAccept(key);
                    }
                    if (key.isReadable()) {
                        handlerRead(key);
                    }
                    keyIterator.remove();
                }

        }

    }

    private void handlerRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        int read = channel.read(buffer);
            if (read == -1) {
                return;
            }
        buffer.flip();
        StringBuilder msg = new StringBuilder();
            while (buffer.hasRemaining()) {
                msg.append((char) buffer.get());
            }
            buffer.clear();
        System.out.println("received: " + msg);

       // buffer.put((char) msg);
        buffer.rewind();
            channel.write(buffer);

    }

    private void handlerAccept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel)key.channel();

            new Thread(() -> {
                while (true) {
                try {
                    SocketChannel socketChannel = channel.accept(); // можно сделать очередь для чата аналог socket  в io
                    socketChannel.write(ByteBuffer.wrap(
                            "Hello to calc server!\n Input two args for calculate sum:\n".getBytes(StandardCharsets.UTF_8)));
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, OP_READ); //селектор имеет не только serverChannal канал, но и этот.
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
         }).start();
    }

    public static void main(String[] args) throws IOException {
        new NioCalculatorServer();
    }
}
